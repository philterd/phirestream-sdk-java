/*******************************************************************************
 * Copyright 2021 Mountain Fog, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.mtnfog.phirestream.services;

import com.mtnfog.phirestream.model.ProduceRequest;
import com.mtnfog.phirestream.model.ProduceResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface PhirestreamService {

	@Headers({"Content-Type: application/vnd.kafka.json.v2+json"})
	@POST("/topics/{topic}")
	Call<ProduceResponse> produce(@Path("topic") String topic,
								  @Query("async") boolean async,
								  @Query("profile") String profile,
								  @Query("context") String context,
								  @Query("id") String id,
								  @Body ProduceRequest produceRequest);

	@Headers({"Content-Type: application/vnd.kafka.json.v2+json"})
	@POST("/topics/{topic}")
	Call<ProduceResponse> produce(@Path("topic") String topic, int partition,
								  @Query("async") boolean async,
								  @Query("profile") String profile,
								  @Query("context") String context,
								  @Query("id") String id,
								  @Body ProduceRequest produceRequest);

}
