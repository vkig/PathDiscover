# PathDiscover
This application is solving a simple recursive directory search for distinct filenames with a given extension.
But the whole infrastructure around the simple logic is more complex. The web application is created in Spring Boot 3.2.3.,
 the database is served by PostgreSQL. The whole application requires Java 17.

If somebody wants to quickly try it then after cloning the repository if [podman](https://podman.io/) and [make](https://www.gnu.org/software/make/)
is installed on the system, then `make run` can be called and the script will build an image for the database, two separate 
images for two web application instances one for port 8081 and another for 8082. And after the building process the script 
will start the images in podman containers with `--systemd always` option. After that it is possible to test the functionality
from the host computer by visiting either of `localhost:8081` or `localhost:8082`.

## Prerequisites

- Git
- Java 17 or above (if you want to run the application on the host computer)
- Podman
- Make
- Python (to run the database healthcheck.py - it can be replaced with less safe sleep 5 in the Makefile)

## Endpoints:

- `/unique`: to find distinct filenames in a given folder with the given extension recursively
  - folder: parameter to specify where to start looking for distinct files in the directory tree
  - extension: parameter to specify the file extension that we are looking for
  - returns: list of distinct filenames
- `/history`: to see the `/unique` endpoint's call history
  - returns: list of log items, which contain information about who, when, with what parameters called the `/unique` endpoint and also show the result of the query
- `/gen`: to generate random directory tree
  - root: parameter to specify where to start random directory tree generation
  - return: success or error message
- `/docs`: redirects to generated, static JavaDoc
- `/swagger-ui`: To see the visual summary of the application

## Quick start:

- `git clone https://github.com/vkig/PathDiscoverer.git`
- `cd PathDiscoverer`
- `make run`
- Visit `localhost:8081/unique?folder=.&extension=java`
- Visit `localhost:8082/history`
