#!/bin/bash

lein cljsbuild once

lein garden once

lein uberjar

java -jar target/challenge-0.1.0-SNAPSHOT-standalone.jar -p 4321