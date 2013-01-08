ACLU AZ - Stop SB 1070
======================

Android app for reporting abuses of the Arizona SB 1070 law to the ACLU of Arizona.


## Downloading the Source
    
When downloading the source make sure to clone the repository with:

    $ git clgit@github.com:OpenWatch/ACLU-AZ-Android.git.git --recursive
    
## Building

In Eclipse, Import an Existing Android Project from Source for each of the submodules (ActionBarSherlock, and androrm . Make sure each project is checked as a Library project (R-click project -> Properties -> Android List item -> Libraries pane), and that all three are added as library dependencies of the main project (Also in the Project Properties Libraries Pane       )).

##### SECRETS.java 

Create a file named `SECRETS.java` in /src/net/openwatch/acluaz with the following content:


	package net.openwatch.acluaz;

	public class SECRETS {
		public static final String SSL_KEYSTORE_PASS = "your_keystore_passwo
See the **Developing** section for an explanation of ACLUAZ-Android's SSL trust scheme.rd";
	}



## Developing

ACLUAZ-Android verifies ssl certificates against those bundled with the app (`AZHttpClient` is hardcoded to /res/raw/azkeystore in this implementation). 

#### To package your server's ssl certificates:

 1. Get your servers X509 format ssl certificates. You'll need the root and all intermediate certificates, but not your local cert. [In my experience this was easiest to do in Firefox](http://superuser.com/a/97203/185405)
 2. [Generate a BKS format keystore with your certificates](http://blog.antoine.li/2010/10/22/android-trusting-ssl-certificates/)
 3. Place your keystore in /res/raw and update `AZHttpClient` with the appropriate filename


