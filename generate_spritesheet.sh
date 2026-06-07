#!/bin/bash

if [ "$#" -lt 2 ]; then
    echo "Usage: ./generate_spritesheet.sh <outputPath> <inputPath1> [inputPath2] ..."
    exit 1
fi

# Compile the generator
mvn compile -pl nomad-realms -am -q

# Construct arguments string with proper quoting for Maven exec:java
# This helps handle paths with spaces
ARGS=""
for arg in "$@"; do
    # Escape double quotes in arguments
    escaped_arg=$(echo "$arg" | sed 's/"/\\"/g')
    ARGS="$ARGS \"$escaped_arg\""
done

# Run the generator
mvn exec:java -pl nomad-realms -Dexec.mainClass="nomadrealms.tool.SpriteSheetGenerator" -Dexec.args="$ARGS" -q
