CREATE TABLE "user_todo"."user_todos" ("todo_id" bigserial NOT NULL, "user_id" bigint NOT NULL, "title" text NOT NULL, "description" text NOT NULL, "finished_at" timestamptz NOT NULL, "created_at" timestamptz NOT NULL DEFAULT now(), "updated_at" timestamptz NOT NULL DEFAULT now(), PRIMARY KEY ("todo_id") , FOREIGN KEY ("user_id") REFERENCES "user_todo"."users"("user_id") ON UPDATE restrict ON DELETE restrict);
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
CREATE TRIGGER "set_user_todo_user_todos_updated_at"
BEFORE UPDATE ON "user_todo"."user_todos"
FOR EACH ROW
EXECUTE PROCEDURE "user_todo"."set_current_timestamp_updated_at"();
COMMENT ON TRIGGER "set_user_todo_user_todos_updated_at" ON "user_todo"."user_todos" 
IS 'trigger to set value of column "updated_at" to current timestamp on row update';
