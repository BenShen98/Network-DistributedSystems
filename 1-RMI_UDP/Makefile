
.PHONY : all
all: rmi udp

.PHONY : rmi
rmi:
	@echo "Building RMI Client/Server..."; \
	cd common; \
	javac -g -classpath . *.java; \
	cd ../rmi; \
	javac -g -classpath .:.. *.java; \
	cd ..; \
#	rmic -classpath . rmi.RMIServer; \

.PHONY : udp
udp:
	@echo "Building UDP Client / Server..."; \
	cd common; \
	javac -g -classpath . *.java; \
	cd ../udp; \
	javac -g -classpath .:.. *.java; \
	cd ..; java udp.UDPServer 6666


.PHONY : clean
clean:
	rm rmi/*.class udp/*.class common/*.class
