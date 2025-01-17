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
package kr.apptimer.dagger.android;

import android.app.PendingIntent;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import kr.apptimer.dagger.android.task.SerializableTask;
import kr.apptimer.database.data.InstalledApplication;

/***
 * Cache storing {@link android.app.PendingIntent} of alarm which removes application
 * @author Singlerr
 */
@Singleton
public final class IntentCache {

  /***
   * String: {@link InstalledApplication#getPackageUri()}
   * PendingIntent: {@link TaskScheduler#scheduleTask(SerializableTask, Date)}
   */
  private HashMap<String, PendingIntent> caches;

  @Inject
  public IntentCache() {}

  public PendingIntent getCachedIntent(String packageUri) {
    return caches.get(packageUri);
  }

  public String getPackageUri(PendingIntent intent) {
    for (Map.Entry<String, PendingIntent> entry : caches.entrySet()) {
      if (entry.getValue().equals(intent)) return entry.getKey();
    }
    return null;
  }

  public void putCache(String packageUri, PendingIntent intent) {
    caches.put(packageUri, intent);
  }
}
