CREATE TABLE "chatroom"."room" ("room_id" serial NOT NULL, "name" text NOT NULL, "created_at" timestamptz NOT NULL DEFAULT now(), PRIMARY KEY ("room_id") , UNIQUE ("name"));
