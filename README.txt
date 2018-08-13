Instructions:

1. Build this project using gradle
2. Navigate to build\distributions directory and unzip the .zip file
3. From the unzipped folder navigate to InterviewProject-1.0-SNAPSHOT\bin
4. Open command prompt in current directory 
5. Run InterviewProject.bat with --file argument set (i.e. InterviewProject.bat --file my\path\to\file.txt)
6. A DB directory hsqldb should appear in the current directory

NOTE:
I did not test running in linux the application plugin auto-generates the linux script too.

The input file should be formatted so that every JSON statement ends with CR, LF, or CRLF

LOGGING:
Modify src\main\resources\logback.xml before building to change logging.