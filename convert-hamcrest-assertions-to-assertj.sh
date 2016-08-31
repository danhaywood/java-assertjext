#!/bin/bash

function usage() {
  echo
  echo "NAME"
  echo "convert-hamcrest-assertions-to-assertj.sh - Convert most Hamcrest assertions to AssertJ assertions"
  echo
  echo "SYNOPSIS"
  echo "convert-hamcrest-assertions-to-assertj.sh [Pattern]"
  echo
  echo "OPTIONS"
  echo " -h --help    this help"
  echo " [Pattern]    a find pattern, default to *Test.java if you don't provide a pattern"
  echo "              don't forget to enclose your pattern with double quotes \"\" to avoid pattern to be expanded by your shell prematurely"
  echo
  echo "EXAMPLE"
  echo " convert-hamcrest-assertions-to-assertj.sh \"*IT.java\""
  exit 0
}

if [ "$1" == "-h" -o "$1" == "--help" ] ;
then
 usage
fi

FILES_PATTERN=${1:-*Test.java}

echo ''
echo "Converting Hamcrest assertions to AssertJ assertions on files matching pattern : $FILES_PATTERN"
echo ''

FILES=`find . -name "$FILES_PATTERN"`

for EXPRESSION in \
's/assertThat(\(.*\),.*is(not(nullValue())))/assertThat(\1).isNotNull()/g' \
's/assertThat(\(.*\),.*is(notNullValue()))/assertThat(\1).isNotNull()/g' \
's/assertThat(\(.*\),.*is(nullValue()))/assertThat(\1).isNull()/g' \
's/assertThat(\(.*\),.*is(false))/assertThat(\1).isFalse()/g' \
's/assertThat(\(.*\),.*is(true))/assertThat(\1).isTrue()/g' \
's/assertThat(\(.*\),.*hasSize(\(.*\)))/assertThat(\1).hasSize(\2)/g' \
's/assertThat(\(.*\),.*hasSize(0))/assertThat(\1).isEmpty()/g' \
's/assertThat(\(.*\),.*empty())/assertThat(\1).isEmpty()/g' \
's/assertThat(\(.*\),.*hasItem(\(.*\))))/assertThat(\1).contains(\2)/g' \
's/assertThat(\(.*\).size(),.*is(\(.*\)))/assertThat(\1).hasSize(\2)/g' \
's/assertThat(\(.*\),.*is(not(equalTo(\(.*\)))))/assertThat(\1).isNotEqualTo(\2)/g' \
's/assertThat(\(.*\),.*is(not(greaterThan(\(.*\)))))/assertThat(\1).isLessThanOrEqualTo(\2)/g' \
's/assertThat(\(.*\),.*is(not(\(.*\))))/assertThat(\1).isNotEqualTo(\2)/g' \
's/assertThat(\(.*\),.*is(greaterThan(\(.*\))))/assertThat(\1).isGreaterThan(\2)/g' \
's/assertThat(\(.*\),.*is(equalTo(\(.*\))))/assertThat(\1).isEqualTo(\2)/g' \
's/assertThat(\(.*\),.*is(\(.*\)))/assertThat(\1).isEqualTo(\2)/g' \
's/assertThat(\(.*\),.*not(containsString(\(.*\))))/assertThat(\1).doesNotContain(\2)/g' \
's/assertThat(\(.*\),.*containsString(\(.*\)))/assertThat(\1).contains(\2)/g' \
's/assertThat(\(.*\),[^a-zA-Z]*\(.*\))/assertThat(\1).is(matchedBy(\2))/g' \
's/org\.junit\.Assert\.assertThat\;/org.assertj.core.api.Assertions.assertThat\;/g'
do
    echo ""
    echo ""
    echo ""
    echo "$EXPRESSION"
    for FILE in $FILES
    do
        echo "  $FILE"
        sed $EXPRESSION $FILE > /tmp/$$ 
        if [ $? -eq 0 ]
        then
            chmod 777 $FILE
            cp /tmp/$$ $FILE
            rm /tmp/$$
        else
            echo "- ERRORED !!!"
        fi
    done
done

echo ""
echo ""
echo "Now optimize imports with your IDE to remove unused imports."
echo "Any remaining assertions you'll need to fix up manually (or extend this script and rerun)"
echo ""

