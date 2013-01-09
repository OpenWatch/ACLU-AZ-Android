ACLU AZ - Stop SB 1070
======================

Android app for reporting abuses of the Arizona SB 1070 law to the ACLU of Arizona.


![Screenshot](https://raw.github.com/OpenWatch/ACLU-AZ-Android/master/screenshots/home_nexus7.png)


## Downloading the Source
    
When downloading the source make sure to clone the repository with:

    $ git clone git@github.com:OpenWatch/ACLU-AZ-Android.git --recursive
    
## Building

In Eclipse, Import an Existing Android Project from Source for each of the submodules (ActionBarSherlock, and androrm . Make sure each project is checked as a Library project (R-click project -> Properties -> Android List item -> Libraries pane), and that all three are added as library dependencies of the main project (Also in the Project Properties Libraries Pane       )).

#### SECRETS.java 

Create a file named `SECRETS.java` in /src/net/openwatch/acluaz with the following content:


	package net.openwatch.acluaz;

	public class SECRETS {
		public static final String SSL_KEYSTORE_PASS = "your_keystore_password";
		public static final String BUGSENSE_API_KEY = "your_bugsense_api_key";
	}
See the **Developing** section for an explanation of ACLUAZ-Android's SSL trust scheme.rd";
	}

#### BugSense
If you won't be using BugSense, comment out the following line of MainActivity:

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//BugSenseHandler.initAndStartSession(getApplicationContext(), SECRETS.BUGSENSE_API_KEY);
		setContentView(R.layout.activity_main);
		this.getSupportActionBar().setDisplayShowHomeEnabled(false);
	}
## Developing

ACLUAZ-Android verifies ssl certificates against those bundled with the app (`AZHttpClient` is hardcoded to /res/raw/azkeystore in this implementation). 

#### To package your server's ssl certificates:

 1. Get your server's X509 format ssl certificates. You'll need the root and all intermediate certificates, but not your local cert. [In my experience this was easiest to do in Firefox](http://superuser.com/a/97203/185405)
 2. [Generate a BKS format keystore with your certificates](http://blog.antoine.li/2010/10/22/android-trusting-ssl-certificates/)
 3. Place your keystore in /res/raw and update `AZHttpClient` with the appropriate filename

## License

Copyright (C) 2013, [OpenWatch FPC](http://openwatch.net)

	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.

If you would like to relicense portions of this code to sell it on the App Store, 
please contact us at [david@openwatch.net](mailto:david@openwatch.net).

