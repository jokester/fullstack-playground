CREATE TABLE "user_todo"."users" ("user_id" bigserial NOT NULL, "email" text NOT NULL, "password_hash" text NOT NULL, "created_at" timestamptz NOT NULL DEFAULT now(), "updated_at" timestamptz NOT NULL DEFAULT now(), "profile" jsonb NOT NULL, PRIMARY KEY ("user_id") );
CREATE OR REPLACE FUNCTION "user_todo"."set_current_timestamp_updated_at"()
RETURNS TRIGGER AS $$
DECLARE
  _new record;
BEGIN
  _new := NEW;
  _new."updated_at" = NOW();
  RETURN _new;
END;
$$ LANGUAGE plpgsql;
CREATE TRIGGER "set_user_todo_users_updated_at"
BEFORE UPDATE ON "user_todo"."users"
FOR EACH ROW
EXECUTE PROCEDURE "user_todo"."set_current_timestamp_updated_at"();
COMMENT ON TRIGGER "set_user_todo_users_updated_at" ON "user_todo"."users" 
IS 'trigger to set value of column "updated_at" to current timestamp on row update';
