//  Created by David Brodsky
//  Copyright (c) 2013 OpenWatch FPC. All rights reserved.
//
//  This file is part of ACLU-AZ-Android.
//
//  ACLU-AZ-Android is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  ACLU-AZ-Android is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with ACLU-AZ-Android.  If not, see <http://www.gnu.org/licenses/>.
package net.openwatch.acluaz.model;

import android.content.Context;

import com.orm.androrm.Model;
import com.orm.androrm.QuerySet;
import com.orm.androrm.field.BooleanField;
import com.orm.androrm.field.CharField;
import com.orm.androrm.field.DoubleField;
import com.orm.androrm.field.IntegerField;


public class Incident extends Model {
	private static final String TAG = "SB1070Incident";

	public CharField first_name = new CharField();
	public CharField last_name = new CharField();
	public CharField address_1 = new CharField();
	public CharField address_2 = new CharField();
	public CharField city = new CharField();
	public CharField state = new CharField();
	public IntegerField zip = new IntegerField(99999);
	public CharField email = new CharField();
	public CharField phone = new CharField();
	
	public CharField agency = new CharField();
	public CharField location = new CharField();
	public CharField date = new CharField();
	public CharField description = new CharField();
	public DoubleField device_lat = new DoubleField();
	public DoubleField device_lon = new DoubleField();
	public CharField uuid = new CharField();
	public IntegerField server_id = new IntegerField();
	public BooleanField submitted = new BooleanField();

	public Incident() {
		super();
	}
	
	public static final QuerySet<Incident> objects(Context context) {
        return objects(context, Incident.class);
    }

}
