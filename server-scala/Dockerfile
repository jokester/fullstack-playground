FROM alpine:3.12
COPY ./stateless-akka-http-current.txz /usr/src/
RUN mkdir -pv /srv && tar xvf /usr/src/stateless-akka-http-current.txz -C /srv/

# https://github.com/corretto/corretto-docker
FROM amazoncorretto:11
COPY --from=0 /srv /srv
# default entrypoint ["bash"] is not overriden
# just execute like command: [ "/PATH/BASH_SCRIPT" ]

# [ ] required to pass cli arguments (NO IDEA WHY THIS DONT WORK)
# CMD ["bash", "-c"]
