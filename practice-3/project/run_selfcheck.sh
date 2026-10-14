#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -encoding UTF-8 -d out src/ru/edu/pr01/*.java
java -cp out ru.edu.pr01.SelfCheck
