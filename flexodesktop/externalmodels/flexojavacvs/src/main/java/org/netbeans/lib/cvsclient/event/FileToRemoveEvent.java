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
package org.netbeans.lib.cvsclient.event;

/**
 * Indicates that a file is to be removed.
 * @author Martin Entlicher
 */
public class FileToRemoveEvent extends CVSEvent {

    /**
     * The path of the file that is going to be removed.
     */
    protected String path;

    /**
     * Construct a FileRemovedEvent
     * @param source the source of the event
     * @param path The path of the file that is going to be removed
     */
    public FileToRemoveEvent(Object source, String path) {
        super(source);
        this.path = path;
    }

    /**
     * Get the path of the file that is going to be removed.
     */
    public String getFilePath() {
        return path;
    }

    /**
     * Fire the event to the event listener. Subclasses should call the
     * appropriate method on the listener to dispatch this event.
     * @param listener the event listener
     */
    @Override
	protected void fireEvent(CVSListener listener) {
        listener.fileToRemove(this);
    }
}