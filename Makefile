SOCIALJPA = java/samples/src/main/resources/socialjpa.properties
SHINDIG = java/common/conf/shindig.properties
WEBXML = java/server/src/main/webapp/WEB-INF/web.xml
WAR = java/server/target/shindig-server-2.0.0.war
TOMCAT = /Library/Tomcat

default:
	@echo "Cleaning and compiling shindig"
	@mvn clean && mvn -Dmaven.test.skip

start:
	@echo "Starting the server"
	@cd java/server && mvn jetty:run

all: production

production:
	@echo "Creating production.war file"
	@echo "socialjpa.properties"
	@cp $(SOCIALJPA)_production $(SOCIALJPA)
	@echo "shindig.properties"
	@cp $(SHINDIG)_production $(SHINDIG)
	@echo "web.xml"
	@cp $(WEBXML)_production $(WEBXML)
	@mvn clean
	@mvn -Dmaven.test.skip && cp $(WAR) production.war \
		&& echo "Move now production.war to ROOT.war for Tomcat"

clean:
	@echo "Cleaning temporal changes"
	@echo "socialjpa.properties"
	@cp $(SOCIALJPA)_development $(SOCIALJPA)
	@echo "shindig.properties"
	@cp $(SHINDIG)_development $(SHINDIG)
	@echo "web.xml"
	@cp $(WEBXML)_development $(WEBXML)
	@if [[ -a production.war ]]; then rm production.war; fi
	@mvn clean


