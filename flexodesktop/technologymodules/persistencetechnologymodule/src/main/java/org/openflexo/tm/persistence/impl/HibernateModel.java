/**
 * 
 */
package org.openflexo.tm.persistence.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.naming.InvalidNameException;

import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.FlexoObserver;
import org.openflexo.foundation.NameChanged;
import org.openflexo.foundation.dm.DMEntity;
import org.openflexo.foundation.dm.DMRepository;
import org.openflexo.foundation.dm.dm.EntityRegistered;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.sg.implmodel.ImplementationModel;
import org.openflexo.foundation.sg.implmodel.LinkableTechnologyModelObject;
import org.openflexo.foundation.sg.implmodel.event.SGAttributeModification;
import org.openflexo.foundation.sg.implmodel.event.SGObjectAddedToListModification;
import org.openflexo.foundation.sg.implmodel.event.SGObjectDeletedModification;
import org.openflexo.foundation.sg.implmodel.event.SGObjectRemovedFromListModification;
import org.openflexo.foundation.xml.ImplementationModelBuilder;
import org.openflexo.tm.persistence.impl.comparator.HibernateLinkableObjectComparator;

/**
 * 
 * @author Nicolas Daniels
 */
public class HibernateModel extends LinkableTechnologyModelObject<DMRepository> implements FlexoObserver {

	public static final String CLASS_NAME_KEY = "hibernate_model";

	protected PersistenceImplementation hibernateImplementation;

	protected Vector<HibernateEntity> entities = new Vector<HibernateEntity>();
	protected HibernateEnumContainer hibernateEnumContainer;
    private Set<HibernateRelationship> sndPassRelationships;
	// ================ //
	// = Constructors = //
	// ================ //

	/**
	 * Build a new Hibernate mode for the specified implementation model builder.<br/>
	 * This constructor is namely invoked during unserialization.
	 * 
	 * @param builder
	 *            the builder that will create this entity
	 */
	public HibernateModel(ImplementationModelBuilder builder) {
		this(builder.implementationModel);
		initializeDeserialization(builder);
	}

	/**
	 * Build a new Hibernate model for the specified implementation model.
	 * 
	 * @param implementationModel
	 *            the implementation model where to create this Hibernate entity
	 */
	protected HibernateModel(ImplementationModel implementationModel) {
		super(implementationModel);
	}

	/**
	 * Build a new Hibernate model for the specified implementation model.
	 * 
	 * @param hibernateImplementation1
	 *            the implementation model where to create this Hibernate entity
	 */
	protected HibernateModel(PersistenceImplementation hibernateImplementation1,
                             DMRepository watchedRepository,
                             String name) {
		super(hibernateImplementation1.getImplementationModel(), watchedRepository);

        setName(name);
        hibernateImplementation1.addToModels(this);
        hibernateImplementation = hibernateImplementation1;
        HibernateEnumContainer hibernateEnumContainer = new HibernateEnumContainer(hibernateImplementation1.getImplementationModel());
        hibernateEnumContainer.setName("Enums");
        setHibernateEnumContainer(hibernateEnumContainer);
        sndPassRelationships = new HashSet<HibernateRelationship>();
        try{
            synchronizeWithLinkedFlexoModelObject();
            for(HibernateRelationship relationship:sndPassRelationships){
                relationship.setDestination(relationship.getTargetHibernateEntity());
            }
        }finally {
            sndPassRelationships = null;
        }
	}

	// =========== //
	// = Methods = //
	// =========== //

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalizeDeserialization(Object builder) {
		super.finalizeDeserialization(builder);
		synchronizeWithLinkedFlexoModelObject();
	}

	/**
	 * @Override
	 */
	@Override
	public String getClassNameKey() {
		return CLASS_NAME_KEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getHasInspector() {
		return true;
	}

	/**
	 * @Override
	 */
	@Override
	public String getFullyQualifiedName() {
		return getPersistenceImplementation().getFullyQualifiedName() + "." + getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void synchronizeWithLinkedFlexoModelObject() {
		if (getLinkedFlexoModelObject() != null) {
			List<LinkableTechnologyModelObject<?>> deletedChildren = new ArrayList<LinkableTechnologyModelObject<?>>();

			Map<DMEntity, LinkableTechnologyModelObject<?>> alreadyCreatedChildren = new HashMap<DMEntity, LinkableTechnologyModelObject<?>>();

            //first : filling maps with deleted FlexoModelObject and already linked FlexoModelObject
            for (HibernateEntity hibernateEntity : this.entities) {
                if (hibernateEntity.getLinkedFlexoModelObject() != null) {
                    alreadyCreatedChildren.put(hibernateEntity.getLinkedFlexoModelObject(), hibernateEntity);
                } else if (hibernateEntity.getWasLinkedAtLastDeserialization()) {
                    deletedChildren.add(hibernateEntity);
                }
            }

			for (HibernateEnum hibernateEnum : getHibernateEnumContainer().getHibernateEnums()) {
				if (hibernateEnum.getLinkedFlexoModelObject() != null) {
					alreadyCreatedChildren.put(hibernateEnum.getLinkedFlexoModelObject(), hibernateEnum);
				} else if (hibernateEnum.getWasLinkedAtLastDeserialization()) {
					deletedChildren.add(hibernateEnum);
				}
			}

			List<LinkableTechnologyModelObject<?>> createdEntities = new ArrayList<LinkableTechnologyModelObject<?>>();
			// Create missing entities & enums and update existing ones
			for (DMEntity entity : getLinkedFlexoModelObject().getEntities().values()) {
				if (!alreadyCreatedChildren.containsKey(entity)) {
					createdEntities.add(createHibernateObjectBasedOnDMEntity(entity, false));
				} else {
					alreadyCreatedChildren.get(entity).synchronizeWithLinkedFlexoModelObject();
				}
			}

			// Remove deleted entities & enums
			for (LinkableTechnologyModelObject<?> hibernateObj : deletedChildren) {
				hibernateObj.delete();
			}

			// Synchronize created entities with their linked Flexo object. Performed now to be sure all objects are created
			for (LinkableTechnologyModelObject<?> entity : createdEntities) {
				entity.synchronizeWithLinkedFlexoModelObject();
			}
		}
	}

	/* ===================== */
	/* ====== Actions ====== */
	/* ===================== */

	public static HibernateModel createNewHibernateModel(String name, PersistenceImplementation hibernateImplementation,
			DMRepository watchedRepository) throws DuplicateResourceException, InvalidNameException {
		return new HibernateModel(hibernateImplementation, watchedRepository, name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {

		for (HibernateEntity hibernateEntity : new Vector<HibernateEntity>(getEntities())) {
			hibernateEntity.delete();
		}

		if (getPersistenceImplementation() != null) {
			getPersistenceImplementation().removeFromModels(this);
		}

		setChanged();
		notifyObservers(new SGObjectDeletedModification<HibernateModel>(this));
		super.delete();
		deleteObservers();
	}

	/**
	 * Add a new Hibernate Entity or Hibernate Enum to this model. The newly created hibernate object is based and linked to the specified
	 * DMEntity. If an Hibernate object was already existing for this DMEntity, nothing is performed.
	 * 
	 * @param dmEntity
	 *            the DMEntity the newly created Hibernate object should be linked to.
	 * @return the created object if any, null otherwise.
	 */
	public LinkableTechnologyModelObject<?> createHibernateObjectBasedOnDMEntity(DMEntity dmEntity) {
		return createHibernateObjectBasedOnDMEntity(dmEntity, true);
	}

	/**
	 * Add a new Hibernate Entity or Hibernate Enum to this model. The newly created hibernate object is based and linked to the specified
	 * DMEntity. If an Hibernate object was already existing for this DMEntity, nothing is performed.
	 * 
	 * @param dmEntity
	 *            the DMEntity the newly created Hibernate object should be linked to.
	 * @param synchronizeWithLinkedObject
	 *            specify if the synchronization with the linked object should occur directly or not (useful if multiple object are created
	 *            which can have link to each others)
	 * @return the created object if any, null otherwise.
	 */
	public LinkableTechnologyModelObject<?> createHibernateObjectBasedOnDMEntity(DMEntity dmEntity, boolean synchronizeWithLinkedObject) {
		if (dmEntity.getIsEnumeration()) {
			if (getHibernateEnumContainer().getHibernateEnum(dmEntity) == null) {
				HibernateEnum hibernateEnum = new HibernateEnum(getImplementationModel(), dmEntity);
				getHibernateEnumContainer().addToHibernateEnums(hibernateEnum);
				if (synchronizeWithLinkedObject) {
					hibernateEnum.synchronizeWithLinkedFlexoModelObject();
				}
				return hibernateEnum;
			}
		} else {
			if (getEntity(dmEntity) == null) {
				HibernateEntity entity = new HibernateEntity(this, dmEntity);
				addToEntities(entity);
				if (synchronizeWithLinkedObject) {
					entity.synchronizeWithLinkedFlexoModelObject();
				}
				return entity;
			}
		}

		return null;
	}

	/**
	 * Sort entities stored in this model by their name.
	 */
	public void sortEntities() {
		Collections.sort(this.entities, new HibernateLinkableObjectComparator());
	}

	/* ============== */
	/* == Observer == */
	/* ============== */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		super.update(observable, dataModification);
		if (observable == getLinkedFlexoModelObject()) {
			if (dataModification instanceof EntityRegistered) {
				createHibernateObjectBasedOnDMEntity((DMEntity) dataModification.newValue());
			}
		}
		if (dataModification instanceof NameChanged) {
			sortEntities();
		}
	}

	/* ===================== */
	/* == Getter / Setter == */
	/* ===================== */

	/**
	 * Return the entities stored in this model. Entities are sorted by name.
	 * 
	 * @return sorted entities stored in this model.
	 */
	public Vector<HibernateEntity> getEntities() {
		return entities;
	}

	public void setEntities(Vector<HibernateEntity> entities) {
		if (requireChange(this.entities, entities)) {
			Vector<HibernateEntity> oldValue = this.entities;

			for (HibernateEntity entity : oldValue) {
				entity.deleteObserver(this);
			}

			this.entities.clear();
            if(entities!=null){
                this.entities.addAll(entities);
            }

			for (HibernateEntity entity : entities) {
				entity.addObserver(this);
			}

			sortEntities();
			setChanged();
			notifyObservers(new SGAttributeModification("entities", oldValue, entities));
		}
	}

	public void addToEntities(HibernateEntity entity) {
		entity.setHibernateModel(this);
		entities.add(entity);

		entity.addObserver(this);
		sortEntities();

		setChanged();
		notifyObservers(new SGObjectAddedToListModification<HibernateEntity>("entities", entity));
	}

	public void removeFromEntities(HibernateEntity entity) {
		if (entities.remove(entity)) {
			entity.deleteObserver(this);
			setChanged();
			notifyObservers(new SGObjectRemovedFromListModification<HibernateEntity>("entities", entity));
		}
	}

	/**
	 * Retrieve the Hibernate Entity with the specified name.
	 * 
	 * @param entityName
	 * @return the retrieved Hibernate Entity if any, null otherwise.
	 */
	public HibernateEntity getEntity(String entityName) {
		for (HibernateEntity entity : entities) {
			if (entity.getName().equals(entityName)) {
				return entity;
			}
		}

		return null;
	}

	/**
	 * Retrieve the Hibernate Entity linked to the specified DMEntity.
	 * 
	 * @param dmEntity
	 * @return the retrieved Hibernate Entity if any, null otherwise.
	 */
	public HibernateEntity getEntity(DMEntity dmEntity) {
		for (HibernateEntity entity : entities) {
			if (entity.getLinkedFlexoModelObject() == dmEntity) {
				return entity;
			}
		}

		return null;
	}

	public HibernateEnumContainer getHibernateEnumContainer() {
		return hibernateEnumContainer;
	}

	public void setHibernateEnumContainer(HibernateEnumContainer hibernateEnumContainer) {
		if (requireChange(this.hibernateEnumContainer, hibernateEnumContainer)) {
			HibernateEnumContainer oldValue = this.hibernateEnumContainer;
			if (oldValue != null) {
				hibernateEnumContainer.setHibernateModel(null);
			}
			this.hibernateEnumContainer = hibernateEnumContainer;
			if (hibernateEnumContainer != null) {
				hibernateEnumContainer.setHibernateModel(this);
			}
			setChanged();
			notifyObservers(new SGAttributeModification("hibernateEnumContainer", oldValue, hibernateEnumContainer));
		}
	}

	public PersistenceImplementation getPersistenceImplementation() {
		return hibernateImplementation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PersistenceImplementation getTechnologyModuleImplementation() {
		return getPersistenceImplementation();
	}

	/**
	 * Called only from HibernateImplementation at deserialisation or at entity creation
	 * 
	 * @param hibernateImplementation
	 */
	protected void setPersistenceImplementation(PersistenceImplementation hibernateImplementation) {
		this.hibernateImplementation = hibernateImplementation;
	}

    public boolean registerRelationshipForSndPass(HibernateRelationship hibernateRelationship) {
        if(sndPassRelationships!=null){
            sndPassRelationships.add(hibernateRelationship);
            return true;
        }
        return false;
    }
}
