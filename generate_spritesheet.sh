#!/bin/bash

# Get the directory of the script
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

if [ "$#" -lt 2 ]; then
    echo "Usage: $0 <outputPath> <inputPath1> [inputPath2] ..."
    exit 1
fi

# Convert all arguments to absolute paths before changing directory
ARGS=""
for arg in "$@"; do
    # If the path is relative, make it absolute based on current directory
    if [[ "$arg" != /* ]]; then
        abs_arg="$(pwd)/$arg"
    else
        abs_arg="$arg"
    fi
    # Escape double quotes
    escaped_arg=$(echo "$abs_arg" | sed 's/"/\\"/g')
    ARGS="$ARGS \"$escaped_arg\""
done

# Change directory to the script's directory so Maven can find the pom.xml
cd "$SCRIPT_DIR" || exit 1

# Compile the generator
mvn compile -pl nomad-realms -am -q

# Run the generator
mvn exec:java -pl nomad-realms -Dexec.mainClass="nomadrealms.tool.SpriteSheetGenerator" -Dexec.args="$ARGS" -q
