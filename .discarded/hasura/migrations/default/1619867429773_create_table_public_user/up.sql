CREATE TABLE "public"."user" ("user_id" serial NOT NULL, "user_email" text NOT NULL, "user_profile" jsonb NOT NULL, PRIMARY KEY ("user_id") , UNIQUE ("user_email"));
