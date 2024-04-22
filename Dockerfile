# Use a base image
FROM debian:bookworm

# Update and install dependencies
RUN apt-get update && apt-get install -y wget

# Create necessary directories and download Nexus and Java 8
WORKDIR /opt
RUN wget https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u402-b06/OpenJDK8U-jdk_aarch64_linux_hotspot_8u402b06.tar.gz
RUN wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz


# Extract Maven & Java 8, and clean up
RUN tar -xf OpenJDK8U-jdk_aarch64_linux_hotspot_8u402b06.tar.gz && rm -f OpenJDK8U-jdk_aarch64_linux_hotspot_8u402b06.tar.gz
RUN tar xzvf apache-maven-3.9.6-bin.tar.gz && rm -f apache-maven-3.9.6-bin.tar.gz


# Set environment variables
ENV JAVA_HOME=/opt/jdk8u402-b06
ENV PATH=$PATH:$JAVA_HOME/bin

# Add maven to path
ENV PATH =$PATH:/opt/apache-maven-3.9.6/bin

WORKDIR /user

COPY . .

# Create .m2 directory
RUN mkdir ~/.m2

#Copy deploy settings in ~/.m2
RUN cat .github/settings.xml > ~/.m2/settings.xml

RUN mvn deploy --settings .github/settings.xml


#CMD ["tail", "-f", "/dev/null"]