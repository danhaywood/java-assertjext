#!/usr/bin/env bash
RELEASE_VERSION=$1
SNAPSHOT_VERSION=$2
KEYID=$3
PASSPHRASE=$4


if [ ! "$RELEASE_VERSION" -o ! "$SNAPSHOT_VERSION" -o ! "$KEYID" -o ! "$PASSPHRASE" ]; then
    echo "usage: $(basename $0) [release_version] [snapshot_version] [keyid] [passphrase]" >&2
    exit 1
fi



echo ""
echo "sanity check (mvn clean install -o)"
echo ""
mvn clean install -o >/dev/null
if [ $? != 0 ]; then
    echo "... failed" >&2
    exit 1
fi


echo ""
echo "bumping version to $RELEASE_VERSION"
echo ""
sh ./bumpver.sh $RELEASE_VERSION
if [ $? != 0 ]; then
    echo "... failed" >&2
    exit 1
fi


echo ""
echo "double-check (mvn clean install -o)"
echo ""
mvn clean install -o >/dev/null
if [ $? != 0 ]; then
    echo "... failed" >&2
    exit 1
fi


echo ""
echo "releasing to Maven Central (mvn clean deploy -P release)"
echo ""
mvn clean deploy -P release -Dpgp.secretkey=keyring:id=$KEYID -Dpgp.passphrase="literal:$PASSPHRASE"
if [ $? != 0 ]; then
    echo "... failed" >&2
    exit 1
fi



echo ""
echo "bumping version to $SNAPSHOT_VERSION"
echo ""
sh ./bumpver.sh $SNAPSHOT_VERSION
if [ $? != 0 ]; then
    echo "... failed" >&2
    exit 1
fi


echo ""
echo "now run:"
echo ""
echo "git push origin master && git push origin $RELEASE_VERSION"
echo ""
