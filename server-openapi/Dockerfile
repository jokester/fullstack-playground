FROM alpine:3.12
COPY ./ /usr/src/
RUN mkdir -pv /srv && tar xvf /usr/src/openapi-server-current.txz -C /srv/

FROM amazoncorretto:11
COPY --from=0 /srv /srv
# [ ] required to pass cli arguments
ENTRYPOINT ["/srv/openapi-server-current/bin/openapi-server"]
