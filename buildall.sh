#!/bin/sh
#
# @file buildall.sh
# @brief rtmtools build script
# @author Noriaki Ando <n-ando@aist.go.jp>
#
# update:
# cerate:Sep/11/2008
#
# * How to build rtmtools
#
# ** Required environment
#
# The following development environment and tools are required to
# build rtmtools.
#
# - jdk
# - ant
# - Eclipse SDK (3.4 or later is required.)
#
# ** Before build tools
#
# Please edit "version" text to set version number of the tools. This
# is a kind of bash script.
#
# Example:
# -------
# VERSION=1.1.0
# PROJECT_VERSION=${VERSION}.rc7v$(date +%Y%m%d)
#
# ** Environment variables
#
# To build rtmtools, some environmental variables can be set. In most
# case, these variables are automatically set.
#
# - ECLIPSE_HOME: A directory path to an Eclipse SDK. Under this
#                 directory, .eclipseproduct, eclipse.ini, plugins and
#                 eclipse executable should exist. If this
#                 env.variable is not set, this script tries to search
#                 eclipse directory under some directories that is set
#                 in a env.variable ECLIPSE_DIRS written in the head
#                 of this script.
#
# - JAVA_HOME: A directory JDK installed. If this variable is not set,
#                 this script tries to estimate JDK directory to
#                 resolve symbolic link of javac executable.
#
# - VERSION: A simple version number for the tools like 1.1.1. This is
#                 used for actual bundles' version number with
#                 PROJECT_VERSION number.
#
# - PROJECT_VERSION: Project version number is full version string
#                 with simple version and suffix like
#                 rc120121212. This version string is used actual jar
#                 file name.
#
# - JARDIR: A directory to store jar files. Default directory is "jar".
#                 This directory is  temporary jar files store place.
#
# - DISTDIR: A directory to be archived for distribution package.
#                 This directory name becomes archive package name.
#                 Default name is openrtm-x.y.z. x, y, z are version
#                 number which is defined in version text file.
#


#---------------------------------------------------------------------------
# Global variables
#---------------------------------------------------------------------------

# Default build taget
TARGET="buildAll"

# Default
JARDIR_DEFAULT="jar"

# target projects
PROJECTS="jp.go.aist.rtm.toolscommon.profiles
    jp.go.aist.rtm.toolscommon.profiles.nl1
    jp.go.aist.rtm.toolscommon
    jp.go.aist.rtm.toolscommon.nl1
    jp.go.aist.rtm.rtcbuilder
    jp.go.aist.rtm.rtcbuilder.nl1
    jp.go.aist.rtm.rtcbuilder.java
    jp.go.aist.rtm.rtcbuilder.python
    jp.go.aist.rtm.repositoryView
    jp.go.aist.rtm.repositoryView.nl1
    jp.go.aist.rtm.nameserviceview
    jp.go.aist.rtm.nameserviceview.nl1
    jp.go.aist.rtm.systemeditor
    jp.go.aist.rtm.systemeditor.nl1"

# Eclipse search directories
ECLIPSE_DIRS="../ ../../ ../../ $HOME /usr/lib/ /usr/share"

#============================================================
# functions
#============================================================

#------------------------------------------------------------
# find_eclipsehome
#
# This function checks ECLIPSE_HOME env variable and if it is
# not set, it searches an eclipse directory under ECLIPSE_DIRS,
# and set ECLIPSE_HOME env variable.
# ------------------------------------------------------------
find_eclipsehome()
{
    if test ! "x$ECLIPSE_HOME" = "x" ; then
        return 0
    fi
    echo "Environment variable ECLIPSE_HOME is not set. Seaching..."
    for d in $ECLIPSE_DIRS ; do
        tmp=`find $d -name .eclipseproduct`
        if test "x$tmp" = "x" ; then
            continue
        fi
        for e in $tmp ; do
            edir=`dirname $e`
            if test -f $edir/eclipse.ini && test -d $edir/plugins ; then
                export ECLIPSE_HOME="$edir"
                return 0
            fi
        done
    done
    echo "eclipse not found. Please install eclipse and set ECLIPSE_HOME."
    exit 1
}

#------------------------------------------------------------
# find_javahome
#
# This function estimate JAVA_HOME from javac, which usually
# is a symbolic link to $JAVA_HOME/bin/javac.
#------------------------------------------------------------
find_javahome()
{
    if test ! "x$JAVA_HOME" = "x" ; then
        return 0
    fi
    echo "Environment variable JAVA_HOME is not set. Searching..."
    tmp=`readlink -e $(which javac)`
    jdk_path=`dirname $tmp | sed 's/\/bin$//'`
    if test "x$jdk_path" = "x" ; then
        echo "JDK not found. Please install JDK and set JAVA_HOME."
        exit 1
    fi
    export JAVA_HOME=$jdk_path
    return 0
}

get_version()
{
    if test "x$VERSION" = "x" || test "x$PROJECT_VERSION" = "x" ; then
        echo "Environment variable VERSION/PROJECT_VERSION is not set."
        echo "Getting from ./version text."
        . ./version
    fi

    if test "x$DISTDIR" = "x" ; then
        DISTDIR=openrtp-$VERSION
    fi
    if test "x$JARDIR" = "x" ; then
        JARDIR=$JARDIR_DEFAULT
    fi
}

#------------------------------------------------------------
# cleanup_jardir
#
# Cleanup jar directory
#------------------------------------------------------------
cleanup_jardir()
{
    if test -f $DISTDIR.zip ; then
        rm -f $DISTDIR.zip
    fi
    if test -d $DISTDIR; then
        rm -rf $DISTDIR
    fi
    if test -d $JARDIR; then
        rm -rf $JARDIR
    fi
    mkdir $DISTDIR
    mkdir $JARDIR
}

do_ant_build()
{
    for project in $PROJECTS; do
        if test -d $project; then
            echo "- Starting ant build of project: $project"
            cd $project
            ant $TARGET $LIBS
            if test $? -ne 0; then
                echo "[ERROR] build failed: " $project
                echo "Aborting..."
                exit 1
            fi
            echo "Copying created jar file into $DISTDIR..."
            cp jar/*aist*.jar ../$DISTDIR
            cp jar/*aist*.jar ../$JARDIR
            cd ..
        else
            echo "Project: $project does not exist"
            echo "Skipping..."
        fi
    done
}

create_zip()
{
    if test -f $DISTDIR.zip ; then
        rm $DISTDIR.zip
    fi
    zip $DISTDIR.zip -r ./$DISTDIR
}

getopt()
{
    if test $# = 1 ; then
        arg=$1
        if test "x$arg" = "xclean" ; then
            TARGET=$1
            return 0
        fi
        if test "x$arg" = "xrevert" ; then
            echo "Reverting updated MANIFEST.MF..."
            mf=`svn status | grep '^M' | grep MANIFEST.MF | awk '{print $2;}'`
            for m in $mf; do
                rm $m
                svn update $m
            done
            exit 0
        fi
    fi
}

#==============================
# main
#==============================
getopt $*
find_eclipsehome
find_javahome
get_version

echo "ECLIPSE_HOME: $ECLIPSE_HOME"
echo "JAVA_HOME: $JAVA_HOME"
echo "VERSION: $VERSION"
echo "PROJECT_VERSION: $PROJECT_VERSION"

LIBS="-lib ../lib -lib $ECLIPSE_HOME/plugins"

cleanup_jardir
do_ant_build
if test "x$TARGET" = "xclean" ; then
    rm -rf $DISTDIR
    rm -rf $JARDIR
    exit 0
fi
create_zip

exit 0

# end of script
