@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Pulse startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and PULSE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS="-Dio.ktor.development=false"

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\main.jar;%APP_HOME%\lib\cloudflare-ktor-master-SNAPSHOT.jar;%APP_HOME%\lib\ktor-server-content-negotiation-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-server-websockets-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-server-auth-jwt-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-server-auth-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-server-cio-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-server-netty-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-server-host-common-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-server-sessions-jvm-2.1.0.jar;%APP_HOME%\lib\logback-classic-1.2.11.jar;%APP_HOME%\lib\kotlingram-bot-builder-jvm-1.2.7.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\exposed-dao-0.37.3.jar;%APP_HOME%\lib\exposed-jdbc-0.37.3.jar;%APP_HOME%\lib\exposed-core-0.37.3.jar;%APP_HOME%\lib\mysql-connector-java-8.0.15.jar;%APP_HOME%\lib\ktor-server-core-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-json-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-client-apache-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-client-auth-jvm-2.1.0.jar;%APP_HOME%\lib\kotlingram-core-jvm-1.2.7.jar;%APP_HOME%\lib\ktor-network-tls-certificates-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-client-java-jvm-1.5.3.jar;%APP_HOME%\lib\ktor-serialization-kotlinx-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-client-serialization-jvm-1.5.1.jar;%APP_HOME%\lib\ktor-client-json-jvm-1.5.1.jar;%APP_HOME%\lib\ktor-client-core-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-websocket-serialization-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-serialization-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-events-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-http-cio-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-websockets-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-http-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-network-tls-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-network-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-utils-jvm-2.1.0.jar;%APP_HOME%\lib\ktor-io-jvm-2.1.0.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.9.22.jar;%APP_HOME%\lib\java-telegram-bot-api-6.8.0.jar;%APP_HOME%\lib\logging-interceptor-4.10.0.jar;%APP_HOME%\lib\okhttp-4.10.0.jar;%APP_HOME%\lib\kotlin-reflect-1.7.0.jar;%APP_HOME%\lib\bcrypt-jvm-2.2.0.jar;%APP_HOME%\lib\kotlinx-coroutines-slf4j-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.6.4.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.6.4.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.3.3.jar;%APP_HOME%\lib\okio-jvm-3.0.0.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.3.3.jar;%APP_HOME%\lib\kotlin-stdlib-1.9.22.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.9.22.jar;%APP_HOME%\lib\mariadb-java-client-2.7.2.jar;%APP_HOME%\lib\gson-2.10.1.jar;%APP_HOME%\lib\jbcrypt-0.4.jar;%APP_HOME%\lib\aspectjweaver-1.9.19.jar;%APP_HOME%\lib\HikariCP-4.0.3.jar;%APP_HOME%\lib\ehcache-3.10.8.jar;%APP_HOME%\lib\telegrambots-5.3.0.jar;%APP_HOME%\lib\eventbus-java-3.3.1.jar;%APP_HOME%\lib\thumbnailator-0.4.20.jar;%APP_HOME%\lib\imageio-jpeg-3.9.4.jar;%APP_HOME%\lib\postgresql-42.7.0.jar;%APP_HOME%\lib\logback-core-1.2.11.jar;%APP_HOME%\lib\telegrambots-meta-5.3.0.jar;%APP_HOME%\lib\slf4j-api-1.7.36.jar;%APP_HOME%\lib\junit-4.10.jar;%APP_HOME%\lib\protobuf-java-3.6.1.jar;%APP_HOME%\lib\cache-api-1.1.0.jar;%APP_HOME%\lib\jaxb-runtime-2.4.0-b180830.0438.jar;%APP_HOME%\lib\jersey-media-json-jackson-2.32.jar;%APP_HOME%\lib\java-jwt-3.19.2.jar;%APP_HOME%\lib\jwks-rsa-0.17.0.jar;%APP_HOME%\lib\jackson-jaxrs-json-provider-2.13.2.jar;%APP_HOME%\lib\jackson-module-jaxb-annotations-2.13.2.jar;%APP_HOME%\lib\jackson-jaxrs-base-2.13.2.jar;%APP_HOME%\lib\jackson-core-2.13.2.jar;%APP_HOME%\lib\jackson-databind-2.13.2.2.jar;%APP_HOME%\lib\jackson-annotations-2.13.2.jar;%APP_HOME%\lib\jersey-hk2-2.32.jar;%APP_HOME%\lib\jersey-container-grizzly2-http-2.32.jar;%APP_HOME%\lib\jersey-server-2.32.jar;%APP_HOME%\lib\json-20180813.jar;%APP_HOME%\lib\httpmime-4.5.13.jar;%APP_HOME%\lib\httpasyncclient-4.1.5.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\commons-io-2.8.0.jar;%APP_HOME%\lib\imageio-metadata-3.9.4.jar;%APP_HOME%\lib\imageio-core-3.9.4.jar;%APP_HOME%\lib\common-image-3.9.4.jar;%APP_HOME%\lib\common-io-3.9.4.jar;%APP_HOME%\lib\common-lang-3.9.4.jar;%APP_HOME%\lib\guava-30.0-jre.jar;%APP_HOME%\lib\checker-qual-3.31.0.jar;%APP_HOME%\lib\config-1.4.2.jar;%APP_HOME%\lib\jansi-2.4.0.jar;%APP_HOME%\lib\netty-codec-http2-4.1.78.Final.jar;%APP_HOME%\lib\alpn-api-1.1.3.v20160715.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.78.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.78.Final.jar;%APP_HOME%\lib\hamcrest-core-1.1.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\bcrypt-0.9.0.jar;%APP_HOME%\lib\jaxb-api-2.4.0-b180830.0359.jar;%APP_HOME%\lib\txw2-2.4.0-b180830.0438.jar;%APP_HOME%\lib\istack-commons-runtime-3.0.7.jar;%APP_HOME%\lib\stax-ex-1.8.jar;%APP_HOME%\lib\FastInfoset-1.2.15.jar;%APP_HOME%\lib\javax.activation-api-1.2.0.jar;%APP_HOME%\lib\jakarta.xml.bind-api-2.3.3.jar;%APP_HOME%\lib\jakarta.activation-api-1.2.2.jar;%APP_HOME%\lib\jersey-client-2.32.jar;%APP_HOME%\lib\jersey-media-jaxb-2.32.jar;%APP_HOME%\lib\jersey-common-2.32.jar;%APP_HOME%\lib\hk2-locator-2.6.1.jar;%APP_HOME%\lib\javassist-3.25.0-GA.jar;%APP_HOME%\lib\jersey-entity-filtering-2.32.jar;%APP_HOME%\lib\hk2-api-2.6.1.jar;%APP_HOME%\lib\hk2-utils-2.6.1.jar;%APP_HOME%\lib\jakarta.inject-2.6.1.jar;%APP_HOME%\lib\grizzly-http-server-2.4.4.jar;%APP_HOME%\lib\jakarta.ws.rs-api-2.1.6.jar;%APP_HOME%\lib\jakarta.annotation-api-1.3.5.jar;%APP_HOME%\lib\jakarta.validation-api-2.0.2.jar;%APP_HOME%\lib\httpcore-nio-4.4.15.jar;%APP_HOME%\lib\httpcore-4.4.15.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\netty-codec-http-4.1.78.Final.jar;%APP_HOME%\lib\netty-handler-4.1.78.Final.jar;%APP_HOME%\lib\netty-codec-4.1.78.Final.jar;%APP_HOME%\lib\netty-transport-classes-kqueue-4.1.78.Final.jar;%APP_HOME%\lib\netty-transport-classes-epoll-4.1.78.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.78.Final.jar;%APP_HOME%\lib\netty-transport-4.1.78.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.78.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.78.Final.jar;%APP_HOME%\lib\netty-common-4.1.78.Final.jar;%APP_HOME%\lib\bytes-1.3.0.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\error_prone_annotations-2.3.4.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\osgi-resource-locator-1.0.3.jar;%APP_HOME%\lib\aopalliance-repackaged-2.6.1.jar;%APP_HOME%\lib\grizzly-http-2.4.4.jar;%APP_HOME%\lib\grizzly-framework-2.4.4.jar


@rem Execute Pulse
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %PULSE_OPTS%  -classpath "%CLASSPATH%" ru.k0ras1k.cloudplanet.ApplicationKt %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable PULSE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%PULSE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
