/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.drm.action;

import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.drm.Author;
import org.openflexo.drm.DocItem;
import org.openflexo.drm.DocItemAction;
import org.openflexo.drm.DocItemVersion;
import org.openflexo.drm.Language;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;

public class RefuseVersion extends FlexoAction {

	private static final Logger logger = Logger.getLogger(RefuseVersion.class.getPackage().getName());

	public static FlexoActionType actionType = new FlexoActionType("refuse_version", FlexoActionType.defaultGroup,
			FlexoActionType.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public FlexoAction makeNewAction(FlexoModelObject focusedObject, Vector globalSelection, FlexoEditor editor) {
			return new RefuseVersion(focusedObject, globalSelection, editor);
		}

		@Override
		protected boolean isVisibleForSelection(FlexoModelObject object, Vector globalSelection) {
			return true;
		}

		@Override
		protected boolean isEnabledForSelection(FlexoModelObject object, Vector globalSelection) {
			return ((object != null) && (object instanceof DocItem) && (getPendingActions((DocItem) object).size() > 0));
		}

	};

	protected static Vector getPendingActions(DocItem item) {
		Vector returned = new Vector();
		for (Enumeration en = item.getDocResourceCenter().getLanguages().elements(); en.hasMoreElements();) {
			Language lang = (Language) en.nextElement();
			DocItemAction dia = item.getLastPendingActionForLanguage(lang);
			if (dia != null) {
				returned.add(dia);
			}
		}
		return returned;
	}

	private DocItem _docItem;
	private DocItemVersion _version;
	private Author _author;
	private DocItemAction _newAction;
	private String _note;
	private Vector<DocItemVersion> _versionsThatCanBeApproved;

	public Vector<DocItemVersion> getVersionsThatCanBeApproved() {
		if (_versionsThatCanBeApproved == null) {
			_versionsThatCanBeApproved = new Vector<DocItemVersion>();
			for (Enumeration en = getPendingActions(getDocItem()).elements(); en.hasMoreElements();) {
				DocItemAction next = (DocItemAction) en.nextElement();
				_versionsThatCanBeApproved.add(next.getVersion());
			}
			;
		}
		return _versionsThatCanBeApproved;
	}

	RefuseVersion(FlexoModelObject focusedObject, Vector globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) {
		if (getVersion() != null) {
			logger.info("RefuseVersion");
			_newAction = getDocItem().refuseVersion(getVersion(), getAuthor(), getDocItem().getDocResourceCenter());
			_newAction.setNote(getNote());
		}
	}

	public DocItem getDocItem() {
		if (_docItem == null) {
			if ((getFocusedObject() != null) && (getFocusedObject() instanceof DocItem)) {
				_docItem = (DocItem) getFocusedObject();
			}
		}
		return _docItem;
	}

	public void setDocItem(DocItem docItem) {
		_docItem = docItem;
	}

	public Author getAuthor() {
		return _author;
	}

	public void setAuthor(Author author) {
		_author = author;
	}

	public DocItemAction getNewAction() {
		return _newAction;
	}

	public DocItemVersion getVersion() {
		return _version;
	}

	public void setVersion(DocItemVersion version) {
		_version = version;
	}

	public String getNote() {
		return _note;
	}

	public void setNote(String note) {
		_note = note;
	}

}
