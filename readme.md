About
==============
This is a version of Apache Shindig 2.0 extended with Spaces for Moodle plugins that
bring OpenSocial gadgets to the Moodle.

Read more and see screenshots at [this blog](http://vohtaski.blogspot.com/2011/09/bring-opensocial-gadgets-to-moodle_22.html)

It works with [this Moodle plugin](https://github.com/vohtaski/shindig-moodle-mod).

Requirements
==============
* Moodle 2.1 (was not checked on previous versions!)
* [Moodle plugin](https://github.com/vohtaski/shindig-moodle-mod)

Installation
==============
Install moodle plugin
--------------
See instructions in [Moodle plugin](https://github.com/vohtaski/shindig-moodle-mod).

Install shindig for moodle
--------------------------
If you want to support OpenSocial APIs, you should
connect your own shindig installation to your Moodle database. You will need to patch the core
Apache Shindig with Moodle-extensions to match OpenSocial APIs with Moodle database schema.
You can find a patch in the code - shindig_moodle.patch.

Get shindig and patch it!  
    
    $git clone git://github.com/vohtaski/moodle-shindig.git
    
    
Add ssl keys
   
    $mkdir ssl_keys
    $cd ssl_keys
    $openssl req -newkey rsa:1024 -days 365 -nodes -x509 -keyout testkey.pem -out testkey.pem -subj '/CN=mytestkey'
    $openssl pkcs8 -in testkey.pem -out oauthkey.pem -topk8 -nocrypt -outform PEM
    
   
Add the ssl keys information into java/common/conf/shindig.properties. Don't forget the full path to your oauthkey.pem!!
    
    shindig.signing.key-name=mytestkey
    shindig.signing.key-file=/path_to_shindig_branch/ssl_keys/oauthkey.pem
    

Add your database information to java/samples/src/main/resources/socialjpa.properties.
    
    db.driver=com.mysql.jdbc.Driver
    db.url=jdbc:mysql://localhost:3306/moodle
    db.user=shindig
    db.password=shindig
    db.write.min=1
    db.read.min=1
    jpa.socialapi.unitname=default
    shindig.canonical.json.db=sampledata/canonicaldb.json
    
    
Change host and port settings for your shindig
    
    # You should specify which shindig host will be run, e.g. , if you want
    # to run the shindig host on your local machine, you should replace the
    # value "iamac71.epfl.ch" with "localhost" in
    # java/server/src/main/webapp/WEB-INF/web.xml line 58
    
    shindig.host=iamac71.epfl.ch
    aKey=/shindig/gadgets/proxy?container=default&amp;url=
    shindig.port=8080
    

Change the url of your moodle installation
    
    # In file: java/samples/src/main/java/org/apache/shindig/social/opensocial/jpa/SpaceDb.java
    # Change MOODLE_URL to your own url
    public static final String MOODLE_URL = "http://iamac71.epfl.ch/moodle";


Change column name in person.db file. Only, if you do not use standard moodle prefix for tables "mdl_"
    
    @Table(name = "mdl_user")
    
Compile and start your server
    
    // To build the project
    $mvn -Dmaven.test.skip
    // To run the server on localhost (if not - put .war file into tomcat)
    $cd java/server
    $mvn jetty:run
    
License
=======

Apache Shindig with Spaces - ASF
------------------------------------------

    /**
     * Licensed to the Apache Software Foundation (ASF) under one
     * or more contributor license agreements. See the NOTICE file
     * distributed with this work for additional information
     * regarding copyright ownership. The ASF licenses this file
     * to you under the Apache License, Version 2.0 (the
     * "License"); you may not use this file except in compliance
     * with the License. You may obtain a copy of the License at
     * 
     *  http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing,
     * software distributed under the License is distributed on an
     * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
     * KIND, either express or implied. See the License for the
     * specific language governing permissions and limitations
     * under the License.
     */
