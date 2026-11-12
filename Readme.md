# Health Record Service

A Spring Boot REST API that fetches and processes hospital Meaningful Use (CEHRT) data from HealthIT.gov. It parses the scorecard dataset, filters records for the year 2014, and returns state-level adoption numbers sorted in descending order.

### Containerization & Deployment
I used Docker with a multi-stage build based on Amazon Corretto 25 (Alpine runtime) to package the application and keep the final image minimal. The image was built locally with the included Maven wrapper and pushed to Docker Hub via the Docker CLI (`docker build` and `docker push`).

* **Docker Hub:** https://hub.docker.com/r/ayushman027/health-record-service

### How to Run
```bash
docker run -d -p 8080:8080 --name health-record-service ayushman027/health-record-service:latest