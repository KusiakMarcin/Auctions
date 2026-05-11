-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler version: 1.0.3
-- PostgreSQL version: 15.0
-- Project Site: pgmodeler.io
-- Model Author: ---
-- object: "Admin" | type: ROLE --
-- DROP ROLE IF EXISTS "Admin";


CREATE ROLE "Admin" WITH 
	CREATEDB
	CREATEROLE
	LOGIN
    PASSWORD 'admin';
CREATE ROLE "User" WITH
    LOGIN


-- ddl-end --


-- Database creation must be performed outside a multi lined SQL file. 
-- These commands were put in this file only as a convenience.
-- 
-- object: new_database | type: DATABASE --
-- DROP DATABASE IF EXISTS new_database;
CREATE DATABASE auctions;
-- ddl-end --

\c auctions

CREATE TYPE public.user_role AS
    ENUM ('ADMIN','USER');
-- ddl-end --
ALTER TYPE public.user_role OWNER TO "Admin";
-- ddl-end --

-- object: public."Aucitons" | type: TABLE --
-- DROP TABLE IF EXISTS public."Aucitons" CASCADE;
CREATE TABLE public."Aucitons" (
                                   "Auction_ID" serial NOT NULL,
                                   "Payment_ID_Payments" integer,
                                   CONSTRAINT "Aucitons_pk" PRIMARY KEY ("Auction_ID")
);
-- ddl-end --
ALTER TABLE public."Aucitons" OWNER TO "Admin";
-- ddl-end --

-- object: public."Bids" | type: TABLE --
-- DROP TABLE IF EXISTS public."Bids" CASCADE;
CREATE TABLE public."Bids" (
                               "Bid_ID" serial NOT NULL,
                               "User_ID_Users" integer,
                               CONSTRAINT "Bids_pk" PRIMARY KEY ("Bid_ID")
);
-- ddl-end --
ALTER TABLE public."Bids" OWNER TO "Admin";
-- ddl-end --

-- object: public."Payments" | type: TABLE --
-- DROP TABLE IF EXISTS public."Payments" CASCADE;
CREATE TABLE public."Payments" (
                                   "Payment_ID" serial NOT NULL,
                                   CONSTRAINT "Payments_pk" PRIMARY KEY ("Payment_ID")
);
-- ddl-end --
ALTER TABLE public."Payments" OWNER TO postgres;
-- ddl-end --

-- object: public."Users" | type: TABLE --
-- DROP TABLE IF EXISTS public."Users" CASCADE;
CREATE TABLE public."Users" (
                                "User_ID" serial NOT NULL,
                                "Role" public.user_role,
                                "Saldo" decimal(15,2) DEFAULT 0.0,
                                "Name" varchar(50),
                                "Last_Name" varchar(50),
                                "Email" varchar(50),
                                "Password" varchar(50),
                                CONSTRAINT "Users_pk" PRIMARY KEY ("User_ID")
);
-- ddl-end --
ALTER TABLE public."Users" OWNER TO "Admin";
-- ddl-end --

-- object: public."Bids_Auction" | type: TABLE --
-- DROP TABLE IF EXISTS public."Bids_Auction" CASCADE;
CREATE TABLE public."Bids_Auction" (
                                       "Bid_ID_Bids" integer NOT NULL,
                                       "Auction_ID_Aucitons" integer NOT NULL,
                                       CONSTRAINT "Bids_Auction_pk" PRIMARY KEY ("Bid_ID_Bids","Auction_ID_Aucitons")
);
-- ddl-end --

-- object: "Bids_fk" | type: CONSTRAINT --
-- ALTER TABLE public."Bids_Auction" DROP CONSTRAINT IF EXISTS "Bids_fk" CASCADE;
ALTER TABLE public."Bids_Auction" ADD CONSTRAINT "Bids_fk" FOREIGN KEY ("Bid_ID_Bids")
    REFERENCES public."Bids" ("Bid_ID") MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "Aucitons_fk" | type: CONSTRAINT --
-- ALTER TABLE public."Bids_Auction" DROP CONSTRAINT IF EXISTS "Aucitons_fk" CASCADE;
ALTER TABLE public."Bids_Auction" ADD CONSTRAINT "Aucitons_fk" FOREIGN KEY ("Auction_ID_Aucitons")
    REFERENCES public."Aucitons" ("Auction_ID") MATCH FULL
    ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: "Users_fk" | type: CONSTRAINT --
-- ALTER TABLE public."Bids" DROP CONSTRAINT IF EXISTS "Users_fk" CASCADE;
ALTER TABLE public."Bids" ADD CONSTRAINT "Users_fk" FOREIGN KEY ("User_ID_Users")
    REFERENCES public."Users" ("User_ID") MATCH FULL
    ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "Payments_fk" | type: CONSTRAINT --
-- ALTER TABLE public."Aucitons" DROP CONSTRAINT IF EXISTS "Payments_fk" CASCADE;
ALTER TABLE public."Aucitons" ADD CONSTRAINT "Payments_fk" FOREIGN KEY ("Payment_ID_Payments")
    REFERENCES public."Payments" ("Payment_ID") MATCH FULL
    ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: "Aucitons_uq" | type: CONSTRAINT --
-- ALTER TABLE public."Aucitons" DROP CONSTRAINT IF EXISTS "Aucitons_uq" CASCADE;
ALTER TABLE public."Aucitons" ADD CONSTRAINT "Aucitons_uq" UNIQUE ("Payment_ID_Payments");
-- ddl-end --

-- object: "Auction_User" | type: CONSTRAINT --
-- ALTER TABLE public."Aucitons" DROP CONSTRAINT IF EXISTS "Auction_User" CASCADE;
ALTER TABLE public."Aucitons" ADD CONSTRAINT "Auction_User" FOREIGN KEY ("Auction_ID")
    REFERENCES public."Users" ("User_ID") MATCH SIMPLE
    ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --

-- object: "User_Payment" | type: CONSTRAINT --
-- ALTER TABLE public."Payments" DROP CONSTRAINT IF EXISTS "User_Payment" CASCADE;
ALTER TABLE public."Payments" ADD CONSTRAINT "User_Payment" FOREIGN KEY ("Payment_ID")
    REFERENCES public."Users" ("User_ID") MATCH SIMPLE
    ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --


