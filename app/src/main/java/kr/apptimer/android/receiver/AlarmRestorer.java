/*
Copyright 2022 singlerr

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

   * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
   * Neither the name of singlerr nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package kr.apptimer.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import kr.apptimer.base.InjectApplicationContext;
import kr.apptimer.dagger.android.ApplicationRemovalExecutor;
import kr.apptimer.dagger.android.NotificationHelper;
import kr.apptimer.dagger.android.TaskScheduler;
import kr.apptimer.dagger.android.task.SerializableTask;
import kr.apptimer.database.LocalDatabase;
import kr.apptimer.database.data.InstalledApplication;

/***
 * Called by android when a device boots.
 * Searches {@link kr.apptimer.database.data.InstalledApplication} from {@link kr.apptimer.database.LocalDatabase} and re-schedule removal task to {@link kr.apptimer.dagger.android.TaskScheduler}
 * @author Singlerr
 */
public final class AlarmRestorer extends BroadcastReceiver {

  @Inject
  NotificationHelper helper;

  @Inject TaskScheduler scheduler;

  @Inject LocalDatabase database;

  @Inject ApplicationRemovalExecutor removalExecutor;

  public AlarmRestorer() {
    super();
    InjectApplicationContext.getInstance().getContext().inject(this);
  }

  @Override
  public void onReceive(Context context, Intent intent) {

    if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
      List<InstalledApplication> reservedApplications =
          database.installedApplicationDao().findAll();
      Date currentTime = Calendar.getInstance().getTime();
      for (InstalledApplication reservedApplication : reservedApplications) {
        if (reservedApplication.getTime().before(currentTime))
          handleOutdatedSchedule(reservedApplication);
        else handleYetSchedule(reservedApplication);
      }
    }
  }

  private void handleOutdatedSchedule(InstalledApplication application) {
    removalExecutor.requestRemoval(application.getPackageUri());
  }

  private void handleYetSchedule(InstalledApplication application) {
    scheduler.scheduleTask(
        application.getPackageUri(),
        (SerializableTask) () -> removalExecutor.requestRemoval(application.getPackageUri()),
        application.getTime());
  }
}
