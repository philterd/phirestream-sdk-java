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
package com.mtnfog.phirestream.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class ProduceResponse {

	@SerializedName("key_schema_id")
	private String keySchemaId;

	@SerializedName("value_schema_id")
	private String valueSchemaId;

	@SerializedName("offsets")
	private List<Offset> offsets = new LinkedList<>();

	public String getKeySchemaId() {
		return keySchemaId;
	}

	public void setKeySchemaId(String keySchemaId) {
		this.keySchemaId = keySchemaId;
	}

	public String getValueSchemaId() {
		return valueSchemaId;
	}

	public void setValueSchemaId(String valueSchemaId) {
		this.valueSchemaId = valueSchemaId;
	}

	public List<Offset> getOffsets() {
		return offsets;
	}

	public void setOffsets(List<Offset> offsets) {
		this.offsets = offsets;
	}

}
