// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2020 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

package com.google.appinventor.client.wizards;

import com.google.appinventor.client.ErrorReporter;
import com.google.appinventor.client.Ode;
import static com.google.appinventor.client.Ode.MESSAGES;
import com.google.appinventor.client.OdeAsyncCallback;
import com.google.appinventor.client.explorer.project.Project;
import com.google.appinventor.client.utils.Uploader;
import com.google.appinventor.client.youngandroid.TextValidators;
import com.google.appinventor.shared.rpc.ServerLayout;
import com.google.appinventor.shared.rpc.UploadResponse;
import com.google.appinventor.shared.rpc.project.UserProject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Wizard for uploading previously archived (downloaded) projects.
 *
 */
public class ProjectUploadWizard extends Wizard {
  // Project archive extension
  private static final String PROJECT_ARCHIVE_EXTENSION = ".aia";

  /**
   * Creates a new project upload wizard.
   */
  public ProjectUploadWizard() {
    super(MESSAGES.projectUploadWizardCaption(), true, false);

    // Initialize UI
    final FileUpload upload = new FileUpload();
    upload.setName(ServerLayout.UPLOAD_PROJECT_ARCHIVE_FORM_ELEMENT);
    upload.getElement().setAttribute("accept", PROJECT_ARCHIVE_EXTENSION);
    setStylePrimaryName("ode-DialogBox");
    VerticalPanel panel = new VerticalPanel();
    panel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
    panel.add(upload);
    addPage(panel);

    // Create finish command (upload a project archive)
    initFinishCommand(new Command() {
      @Override
      public void execute() {
        String filename = upload.getFilename();
        if (filename.endsWith(PROJECT_ARCHIVE_EXTENSION)) {
          // Strip extension and leading path off filename. We need to support both Unix ('/') and
          // Windows ('\\') path separators. File.pathSeparator is not available in GWT.
          filename = filename.substring(0, filename.length() - PROJECT_ARCHIVE_EXTENSION.length()).
              substring(Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\')) + 1);

          // Make sure the project name is legal and unique.
          if (!TextValidators.checkNewProjectName(filename, true)) {
         
            String suggestedName = "" , title;
        	// Show Dialog Box and rename the project
            if (TextValidators.isTitleReserved()) {
              title = MESSAGES.reservedTitleFormatError() + " : " + filename;
            } else {
              filename = filename.replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");
              suggestedName = GetSuggestedName(filename);
              if (!TextValidators.checkNewProjectName(suggestedName, true)) {
                  suggestedName = "";
                  if(TextValidators.isTitleDuplicate()) {
                    title = MESSAGES.duplicateTitleFormatError() + " : " + filename;
                  } else if (TextValidators.isTitleInvalid()) {
                	title = MESSAGES.invalidTitleFormatError() + " : " + filename;
                  } else {
                	title = MESSAGES.reservedTitleFormatError() + " : " + filename;
                  }
              } else {
                title = MESSAGES.suggestNameTitleCaption();
              }
            }
        	  
          new RequestNewProjectNameWizard(new RequestProjectNewNameInterface() {
            @Override
            public void GetNewName(String name) {
              Upload(upload ,name);
            }
          }, suggestedName, title);
        	  
          } else {
        	Upload(upload,filename);
          }
        } else {
          Window.alert(MESSAGES.notProjectArchiveError());
          center();
        }
      }
    });
  }
  
  private void Upload(FileUpload upload, String filename) {
    String uploadUrl = GWT.getModuleBaseURL() + ServerLayout.UPLOAD_SERVLET + "/" +
          ServerLayout.UPLOAD_PROJECT + "/" + filename;
	Uploader.getInstance().upload(upload, uploadUrl,
	 new OdeAsyncCallback<UploadResponse>(
			// failure message
			MESSAGES.projectUploadError()) {
		  @Override
		  public void onSuccess(UploadResponse uploadResponse) {
			switch (uploadResponse.getStatus()) {
			  case SUCCESS:
                String info = uploadResponse.getInfo();
                UserProject userProject = UserProject.valueOf(info);
                Ode ode = Ode.getInstance();
                Project uploadedProject = ode.getProjectManager().addProject(userProject);
                ode.openYoungAndroidProjectInDesigner(uploadedProject);
                break;
              case NOT_PROJECT_ARCHIVE:
                // This may be a "severe" error; but in the
                // interest of reducing the number of red errors, the 
                // line has been changed to report info not an error.
                // This error is triggered when the user attempts to
                // upload a zip file that is not a project.
                ErrorReporter.reportInfo(MESSAGES.notProjectArchiveError());
                break;
              default:
                ErrorReporter.reportError(MESSAGES.projectUploadError());
                break;
            }
          }
    });
  }

  public static String GetSuggestedName(String filename) {
    // if hello,hello4 are in project list and user tries to upload 
	// another project hello.aia then it suggests hello5
		  
    int max = 1;
	int len = filename.length();
	int lastInt = filename.charAt(len-1);
	int splitPosition = -1;
	if (lastInt <= 57 && lastInt >= 48) {
	  splitPosition = len - 1;
	  for (int i = len-2 ; i>=0 ; i--) {
		int cur = filename.charAt(i);
		if (cur <= 57 && cur >=48) splitPosition--; 
		else break;
	  }
	  
	  if (splitPosition == 0) return filename;
	  filename = filename.substring(0, splitPosition);
	}
	for(Project proj: Ode.getInstance().getProjectManager().getProjects(filename)) {
	  String sub = proj.getProjectName().substring(len);
	  if (sub.length() > 0) { 
	    try {
		  lastInt = Integer.parseInt(sub);
		} catch (Exception e) {
		  lastInt = -1 ;
		}
		if (lastInt>max) max = lastInt;
	  }	 
	}
	max++;
	return filename + max;
  }
  
  @Override
  public void show() {
    super.show();
    // Wizard size (having it resize between page changes is quite annoying)
    int width = 320;
    int height = 40;
    this.center();

    setPixelSize(width, height);
    super.setPagePanelHeight(40);
  }
}
