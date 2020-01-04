[![API](https://img.shields.io/badge/API-17%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=17)
[ ![Download](https://api.bintray.com/packages/tcqq/android/timelineview/images/download.svg?version=2.1.2) ](https://bintray.com/tcqq/android/timelineview/2.1.2/link)
[![Licence](https://img.shields.io/badge/Licence-Apache2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# TimelineView

Android Timeline View Library (Using RecyclerView) is simple implementation used to display view like Tracking of shipment/order, steppers etc.

# Usage
Supported attributes with _default_ values:
``` 
<com.tcqq.timelineview.TimelineView
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android usual attrs
    (see below).../>
```
|**Attrs**|**Default** |
|:---|:---|
| `timeline_marker` | `@drawable/marker`
| `timeline_marker_size` | `25dp`
| `timeline_marker_padding_top` | `0dp`
| `timeline_marker_padding_bottom` | `0dp`
| `timeline_marker_in_center` | `false`
| `timeline_start_line_color` | `@color/android.R.color.darker_gray`
| `timeline_end_line_color` | `@color/android.R.color.darker_gray`
| `timeline_marker_color` | `@color/colorAccent`
| `timeline_line_width` | `2dp`
| `timeline_line_style` | `normal`
| `timeline_line_style_dash_gap` | `4dp`
| `timeline_line_style_dash_length` | `8dp`
| `timeline_line_padding` | `0dp`

#### RecyclerView Holder:

   Your `RecyclerViewHolder` should have an extra parameter in constructor i.e viewType from `onCreateViewHolder`. You would also have to call the method `initLine(viewType)` in constructor definition.
 
```
    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
        }
    }
```

#### RecyclerView Adapter:

   override `getItemViewType` method in Adapter
 
```
    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }
```
   And pass the `viewType` from `onCreateViewHolder` to its Holder.
   
```
    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

```

# Setup
#### build.gradle
```
repositories {
    jcenter()
}
```
```
dependencies {
    // Using JCenter
    implementation 'com.tcqq.android:timelineview:2.1.2'
}
```

# Screenshots

![Screenshot](/screenshots/screenshot.png)

License
-------

Copyright 2020 Vipul Asri, Alan Perry.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

  <http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
