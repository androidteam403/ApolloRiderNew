package com.ahmadrosid.lib.drawroutemap;

import com.google.android.gms.maps.model.Polyline;

public interface TaskLoadedCallback {
    Polyline onTaskDone(Object... values);

    Polyline onSecondTaskDone(Object... values);
}
