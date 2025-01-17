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
package kr.apptimer.base;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kr.apptimer.android.receiver.ApplicationInstallationReceiver;
import kr.apptimer.dagger.context.ActivityContext;
import kr.apptimer.dagger.context.ApplicationContext;

/***
 * Base activity class
 *
 * @author Singlerr
 */
public abstract class InjectedAppCompatActivity extends AppCompatActivity {


  private ActivityContext activityContext;

  /***
   * Called after calling {@link kr.apptimer.dagger.context.ActivityContext#inject(any extends InjectedAppCompatActivity)} in context of {@link #onCreate(Bundle)}
   * @param savedInstanceState
   */
  public abstract void onActivityCreate(@Nullable Bundle savedInstanceState);

  /***
   * Register listener for {@link android.view.View} here
   */
  public abstract void bindListeners();
  /***
   * Fill the method body to inject subclass of this using {@param context}
   * @param context {@link ActivityContext}
   */
  protected abstract void inject(ActivityContext context);

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    activityContext = getContext().activityContextFactory().create();
    inject(activityContext);
    onActivityCreate(savedInstanceState);
  }


  protected ApplicationContext getContext() {
    return ((InjectApplicationContext) getApplication()).getContext();
  }

}
