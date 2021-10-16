FROM alpine:3.12
COPY ./stateless-akka-http-current.txz /usr/src/
RUN mkdir -pv /srv && tar xvf /usr/src/stateless-akka-http-current.txz -C /srv/

FROM amazoncorretto:11
COPY --from=0 /srv /srv
# [ ] required to pass cli arguments # NO IDEA WHY THIS DONT WORK
