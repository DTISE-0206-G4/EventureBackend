
-- -----------------------------------
-- "public"."user"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."user" CASCADE;
CREATE TABLE "public"."user" (
                                 "id" SERIAL,
                                 "name" CHARACTER VARYING(255) NOT NULL,
                                 "email" CHARACTER VARYING(255) NOT NULL,
                                 "password" CHARACTER VARYING(255) NOT NULL,
                                 "referral_code" CHARACTER VARYING(255) NOT NULL,
                                 "description" TEXT,
                                 "profile_image" CHARACTER VARYING(255),
                                 "referrer_id" INTEGER,
                                 "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "deleted_at" TIMESTAMP WITH TIME ZONE,
                                 PRIMARY KEY ( "id" ),
                                 CONSTRAINT "user_name_email_referral_code_key" UNIQUE ( "name", "email", "referral_code" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."role"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."role" CASCADE;
CREATE TABLE "public"."role" (
                                 "id" SERIAL NOT NULL,
                                 "name" CHARACTER VARYING(255) NOT NULL,
                                 "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "deleted_at" TIMESTAMP WITH TIME ZONE,
                                 PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."event"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."event" CASCADE;
CREATE TABLE "public"."event" (
                                  "id" SERIAL,
                                  "user_id" INTEGER NOT NULL,
                                  "title" CHARACTER VARYING(255) NOT NULL,
                                  "description" TEXT,
                                  "start_time" TIMESTAMP WITH TIME ZONE NOT NULL,
                                  "end_time" TIMESTAMP WITH TIME ZONE NOT NULL,
                                  "location" TEXT NOT NULL,
                                  "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "deleted_at" TIMESTAMP WITH TIME ZONE,
                                  PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."transaction"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."transaction" CASCADE;
CREATE TABLE "public"."transaction" (
                                        "id" SERIAL NOT NULL,
                                        "user_id" INTEGER NOT NULL,
                                        "ticket_id" INTEGER NOT NULL,
                                        "ticket_price" DOUBLE PRECISION NOT NULL,
                                        "total_price" DOUBLE PRECISION NOT NULL DEFAULT 0,
                                        "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        "deleted_at" TIMESTAMP WITH TIME ZONE,
                                        PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."event_discount"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."event_discount" CASCADE;
CREATE TABLE "public"."event_discount" (
                                           "id" SERIAL NOT NULL,
                                           "event_id" INTEGER NOT NULL,
                                           "title" CHARACTER VARYING(255) NOT NULL,
                                           "description" TEXT,
                                           "code" CHARACTER VARYING(255) NOT NULL,
                                           "amount" DOUBLE PRECISION NOT NULL,
                                           "is_percentage" BOOLEAN NOT NULL DEFAULT false,
                                           "available" INTEGER NOT NULL,
                                           "used" INTEGER NOT NULL DEFAULT 0,
                                           "created_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                                           "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                                           "deleted_at" TIMESTAMP WITH TIME ZONE,
                                           "expired_at" TIMESTAMP WITH TIME ZONE,
                                           PRIMARY KEY ( "id" ),
                                           CONSTRAINT "discount_code_code_key" UNIQUE ( "code" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."user_discount"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."user_discount" CASCADE;
CREATE TABLE "public"."user_discount" (
                                          "id" SERIAL NOT NULL,
                                          "user_id" INTEGER NOT NULL,
                                          "title" CHARACTER VARYING(255) NOT NULL,
                                          "description" TEXT,
                                          "amount" DOUBLE PRECISION NOT NULL DEFAULT 0,
                                          "is_percentage" BOOLEAN NOT NULL,
                                          "code" CHARACTER VARYING(255) NOT NULL,
                                          "is_used" BOOLEAN,
                                          "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "deleted_at" TIMESTAMP WITH TIME ZONE,
                                          "expired_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() + INTERVAL '30 days',
                                          PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."user_roles"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."user_roles" CASCADE;
CREATE TABLE "public"."user_roles" (
                                       "id" SERIAL NOT NULL,
                                       "user_id" INTEGER NOT NULL,
                                       "role_id" INTEGER NOT NULL,
                                       "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       "deleted_at" TIMESTAMP WITH TIME ZONE,
                                       PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."category"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."category" CASCADE;
CREATE TABLE "public"."category" (
                                     "id" SERIAL NOT NULL,
                                     "name" CHARACTER VARYING(255),
                                     "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     "deleted_at" TIMESTAMP WITH TIME ZONE,
                                     PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."event_categories"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."event_categories" CASCADE;
CREATE TABLE "public"."event_categories" (
                                             "id" SERIAL NOT NULL,
                                             "event_id" INTEGER NOT NULL,
                                             "category_id" INTEGER NOT NULL,
                                             "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             "deleted_at" TIMESTAMP WITH TIME ZONE,
                                             PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."trx_event_discounts"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."trx_event_discounts" CASCADE;
CREATE TABLE "public"."trx_event_discounts" (
                                                "id" SERIAL NOT NULL,
                                                "transaction_id" INTEGER NOT NULL,
                                                "event_discount_id" INTEGER NOT NULL,
                                                "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                "deleted_at" TIMESTAMP WITH TIME ZONE,
                                                PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."trx_user_discounts"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."trx_user_discounts" CASCADE;
CREATE TABLE "public"."trx_user_discounts" (
                                               "id" SERIAL NOT NULL,
                                               "transaction_id" INTEGER NOT NULL,
                                               "user_discount_id" INTEGER NOT NULL,
                                               "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                                               "deleted_at" TIMESTAMP WITH TIME ZONE,
                                               PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."review"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."review" CASCADE;
CREATE TABLE "public"."review" (
                                   "id" SERIAL NOT NULL,
                                   "event_id" INTEGER NOT NULL,
                                   "user_id" INTEGER NOT NULL,
                                   "stars" INTEGER NOT NULL DEFAULT 5,
                                   "description" TEXT,
                                   "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   "deleted_at" TIMESTAMP WITH TIME ZONE,
                                   PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."event_images"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."event_images" CASCADE;
CREATE TABLE "public"."event_images" (
                                         "id" SERIAL NOT NULL,
                                         "event_id" INTEGER NOT NULL,
                                         "image_url" CHARACTER VARYING NOT NULL,
                                         "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "deleted_at" TIMESTAMP WITH TIME ZONE NOT NULL,
                                         PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."review_images"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."review_images" CASCADE;
CREATE TABLE "public"."review_images" (
                                          "id" SERIAL NOT NULL,
                                          "review_id" INTEGER NOT NULL,
                                          "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "deleted_at" TIMESTAMP WITH TIME ZONE,
                                          PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- "public"."ticket"
-- -----------------------------------

DROP TABLE IF EXISTS "public"."ticket" CASCADE;
CREATE TABLE "public"."ticket" (
                                   "id" SERIAL NOT NULL,
                                   "event_id" INTEGER NOT NULL,
                                   "name" CHARACTER VARYING(255) NOT NULL,
                                   "price" DOUBLE PRECISION NOT NULL,
                                   "available_seat" INTEGER NOT NULL,
                                   "sold_seat" INTEGER NOT NULL DEFAULT 0,
                                   "created_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   "updated_at" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   "deleted_at" TIMESTAMP WITH TIME ZONE,
                                   PRIMARY KEY ( "id" )
)
    WITH ( OIDS=FALSE );

-- -----------------------------------
-- Foreign Keys
-- -----------------------------------

ALTER TABLE "public"."event"
    ADD CONSTRAINT "event_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."transaction"
    ADD CONSTRAINT "transaction_ticket_id_fkey" FOREIGN KEY ( "ticket_id" ) REFERENCES "public"."ticket" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."event_discount"
    ADD CONSTRAINT "discount_code_event_id_fkey" FOREIGN KEY ( "event_id" ) REFERENCES "public"."event" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."user_discount"
    ADD CONSTRAINT "user_discount_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."user_roles"
    ADD CONSTRAINT "user_roles_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."user_roles"
    ADD CONSTRAINT "user_roles_role_id_fkey" FOREIGN KEY ( "role_id" ) REFERENCES "public"."role" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."event_categories"
    ADD CONSTRAINT "event_categories_category_id_fkey" FOREIGN KEY ( "category_id" ) REFERENCES "public"."category" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."event_categories"
    ADD CONSTRAINT "event_categories_event_id_fkey" FOREIGN KEY ( "event_id" ) REFERENCES "public"."event" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."trx_event_discounts"
    ADD CONSTRAINT "trx_event_discounts_transaction_id_fkey" FOREIGN KEY ( "transaction_id" ) REFERENCES "public"."transaction" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."trx_event_discounts"
    ADD CONSTRAINT "trx_event_discounts_event_discount_id_fkey" FOREIGN KEY ( "event_discount_id" ) REFERENCES "public"."event_discount" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."trx_user_discounts"
    ADD CONSTRAINT "trx_user_discounts_transaction_id_fkey" FOREIGN KEY ( "transaction_id" ) REFERENCES "public"."transaction" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."trx_user_discounts"
    ADD CONSTRAINT "trx_user_discounts_user_discount_id_fkey" FOREIGN KEY ( "user_discount_id" ) REFERENCES "public"."user_discount" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."review"
    ADD CONSTRAINT "review_user_id_fkey" FOREIGN KEY ( "user_id" ) REFERENCES "public"."user" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."review"
    ADD CONSTRAINT "review_event_id_fkey" FOREIGN KEY ( "event_id" ) REFERENCES "public"."event" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."event_images"
    ADD CONSTRAINT "event_images_event_id_fkey" FOREIGN KEY ( "event_id" ) REFERENCES "public"."event" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."review_images"
    ADD CONSTRAINT "review_images_review_id_fkey" FOREIGN KEY ( "review_id" ) REFERENCES "public"."review" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE "public"."ticket"
    ADD CONSTRAINT "ticket_event_id_fkey" FOREIGN KEY ( "event_id" ) REFERENCES "public"."event" ( "id" )
        ON DELETE NO ACTION ON UPDATE NO ACTION;
