#!/bin/sh
#===========================================================================
# update:
# cerate:Sep/11/2008
# 
# このシェルスクリプトは RTSystemEditor をビルドします。
#     （以降、"Update"毎に上記に日付、名前、変更内容を記述する）
# このシェルスクリプトは RTSystemEditor をビルドします。
# 環境
#  以下にビルドに必要な環境とインストール方法を簡単に示します。
#   Eclipse SDK
#   ant
#   ant4eclipse
#   jdk
# 
# 環境変数
#  ビルドに必要な環境変数を以下に示します。
#   ECLIPSE_HOME   Eclipse SDK 3.2.x の実行ファイルがあるディレクトリを指定�
#   ECLIPSE33_HOME Eclipse SDK 3.3.x の実行ファイルがあるディレクトリを指定�
#   ANT_HOME       ant のディレクトリを指定します。
#   JAVA_HOME      jdkのディレクトリを指定します。
#
#===========================================================================
#
#

#---------------------------------------------------------------------------
#---------------------------------------------------------------------------
set DUMMY=$ANT_HOME
export ANT_HOME=$ECLIPSE_HOME/plugins/org.apache.ant_1.6.5/


#---------------------------------------------------------------------------
#
#
#
#---------------------------------------------------------------------------
if [ -d jp.go.aist.rtm.fsmcbuilder ]
then
    echo "-jp.go.aist.rtm.fsmcbuilder "
    cd jp.go.aist.rtm.fsmcbuilder 
    ant buildAll -lib $ECLIPSE_HOME/plugins/net.sf.ant4eclipse.plugin_0.5.0.rc1/lib/ -lib $ECLIPSE_HOME/plugins/org.apache.ant_1.6.5/lib/ -lib $ECLIPSE_HOME/plugins/org.junit_3.8.1/ -lib $ECLIPSE_HOME/plugins
    if [ $? -ne 0 ];
    then 
        set ANT_HOME=$DUMMY
        exit 1
    fi
    cd ..
else
    echo "jp.go.aist.rtm.fsmcbuilder doesn't exist."
fi

#---------------------------------------------------------------------------
#
#
#
#---------------------------------------------------------------------------
if [ -d jp.go.aist.rtm.toolscommon.profiles ]
then
    echo "-jp.go.aist.rtm.toolscommon.profiles"
    cd jp.go.aist.rtm.toolscommon.profiles
    ant buildAll -lib $ECLIPSE_HOME/plugins/net.sf.ant4eclipse.plugin_0.5.0.rc1/lib/ -lib $ECLIPSE_HOME/plugins/org.apache.ant_1.6.5/lib/ -lib $ECLIPSE_HOME/plugins/org.junit_3.8.1/ -lib $ECLIPSE_HOME/plugins
    if [ $? -ne 0 ];
    then 
        set ANT_HOME=$DUMMY
         exit 1
    fi
    cd ..
else
    echo "jp.go.aist.rtm.toolscommon.profiles doesn't exist."
fi


#---------------------------------------------------------------------------
#
#
#
#---------------------------------------------------------------------------
if [ -d jp.go.aist.rtm.toolscommon ]
then
    echo "-jp.go.aist.rtm.toolscommon"
    cd jp.go.aist.rtm.toolscommon
    ant buildAll -lib $ECLIPSE_HOME/plugins/net.sf.ant4eclipse.plugin_0.5.0.rc1/lib/ -lib $ECLIPSE_HOME/plugins/org.apache.ant_1.6.5/lib/ -lib $ECLIPSE_HOME/plugins/org.junit_3.8.1/ -lib $ECLIPSE_HOME/plugins
    if [ $? -ne 0 ];
    then 
        set ANT_HOME=$DUMMY
        exit 1
    fi
    cd ..
else
    echo "jp.go.aist.rtm.toolscommon doesn't exist."
fi

#---------------------------------------------------------------------------
#
#
#
#---------------------------------------------------------------------------
if [ -d jp.go.aist.rtm.nameserviceview ]
then
    echo "-jp.go.aist.rtm.nameserviceview"
    cd jp.go.aist.rtm.nameserviceview 
    ant buildAll -lib $ECLIPSE_HOME/plugins/net.sf.ant4eclipse.plugin_0.5.0.rc1/lib/ -lib $ECLIPSE_HOME/plugins/org.apache.ant_1.6.5/lib/ -lib $ECLIPSE_HOME/plugins/org.junit_3.8.1/ -lib $ECLIPSE_HOME/plugins
    if [ $? -ne 0 ];
    then 
        set ANT_HOME=$DUMMY
         exit 1
    fi
    cd ..
else
    echo "jp.go.aist.rtm.nameserviceview doesn't exist."
fi


#---------------------------------------------------------------------------
#
#
#
#---------------------------------------------------------------------------
if [ -d jp.go.aist.rtm.repositoryView ]
then
    echo "-jp.go.aist.rtm.repositoryView"
    cd jp.go.aist.rtm.repositoryView
    ant buildAll -lib $ECLIPSE_HOME/plugins/net.sf.ant4eclipse.plugin_0.5.0.rc1/lib/ -lib $ECLIPSE_HOME/plugins/org.apache.ant_1.6.5/lib/ -lib $ECLIPSE_HOME/plugins/org.junit_3.8.1/ -lib $ECLIPSE_HOME/plugins
    if [ $? -ne 0 ];
    then 
        set ANT_HOME=$DUMMY
         exit 1
    fi
    cd ..
else
    echo "jp.go.aist.rtm.repositoryView doesn't exist."
fi

#---------------------------------------------------------------------------
#
#
#
#---------------------------------------------------------------------------
if [ -d jp.go.aist.rtm.systemeditor ]
then
    echo "-jp.go.aist.rtm.systemeditor"
    cd jp.go.aist.rtm.systemeditor
    ant buildAll -lib $ECLIPSE_HOME/plugins/net.sf.ant4eclipse.plugin_0.5.0.rc1/lib/ -lib $ECLIPSE_HOME/plugins/org.apache.ant_1.6.5/lib/ -lib $ECLIPSE_HOME/plugins/org.junit_3.8.1/ -lib $ECLIPSE_HOME/plugins
    if [ $? -ne 0 ];
    then 
        set ANT_HOME=$DUMMY
        exit 1
    fi
    echo "--"
    cd ..
else
    echo "jp.go.aist.rtm.systemeditor doesn't exist."
fi
#---------------------------------------------------------------------------
# 必要なファイルをzip
#
#
#---------------------------------------------------------------------------
find ./ -name '*aist*.jar' -exec cp -p {} . \;
rm RTSystemEditor.zip 
zip RTSystemEditor.zip *aist*.jar 
rm *aist*.jar



#---------------------------------------------------------------------------
#---------------------------------------------------------------------------
set ANT_HOME=$DUMMY


