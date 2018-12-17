#!/bin/bash

if [[ $OSTYPE == "darwin" ]]; then
  echo "Good child. You are on a Mac"
  if xcode-select --install 2>&1 | grep installed; then
    echo "Even better child. You have Xcode. Building Xcode Projects..."
    {
      xcodebuild -project mobileios/REUNITE.xcodeproj -alltargets -configuration Release
    }
    echo "Done."
    echo "Building Android projects..."
    {
      cd kiosk/
      ./gradlew 
  else
    echo "You need the Xcode to build the project. Exiting..."
    exit 1
  fi
else
  echo "You need a Mac to build the project. Exiting..."
  exit 1
fi
