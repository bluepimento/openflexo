/**
 * 
 */
package org.openflexo.fps.controller;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.openflexo.components.browser.BrowserFilter.BrowserFilterStatus;
import org.openflexo.icon.FPSIconLibrary;

public class RemotelyModifiedPerspective extends FPSPerspective {

	/**
	 * 
	 */
	private final FPSController fpsController;

	public RemotelyModifiedPerspective(FPSController fpsController) {
		super(fpsController, "remotely_modified_files");
		this.fpsController = fpsController;
	}

	@Override
	public ImageIcon getActiveIcon() {
		return FPSIconLibrary.FPS_RMP_ACTIVE_ICON;
	}

	@Override
	public ImageIcon getSelectedIcon() {
		return FPSIconLibrary.FPS_RMP_SELECTED_ICON;
	}

	@Override
	public JPanel getFooter() {
		return this.fpsController._footer;
	}

	@Override
	public void setFilters() {
		this.fpsController.getSharedProjectBrowser().getAllFilesAndDirectoryFilter().setStatus(BrowserFilterStatus.HIDE);
		this.fpsController.getSharedProjectBrowser().getUpToDateFilesFilter().setStatus(BrowserFilterStatus.HIDE);
		this.fpsController.getSharedProjectBrowser().getLocallyModifiedFilter().setStatus(BrowserFilterStatus.HIDE);
		this.fpsController.getSharedProjectBrowser().getRemotelyModifiedFilter().setStatus(BrowserFilterStatus.SHOW);
		this.fpsController.getSharedProjectBrowser().getConflictingFileFilter().setStatus(BrowserFilterStatus.SHOW);
		this.fpsController.getSharedProjectBrowser().getIgnoredFileFilter().setStatus(BrowserFilterStatus.HIDE);
	}

}