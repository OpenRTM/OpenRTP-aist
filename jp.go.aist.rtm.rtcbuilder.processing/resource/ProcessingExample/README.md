# ProcessingExample
## Overview
ModuleDescription
## Description
概要説明
### Input and Output
入出力
### Algorithm etc
アルゴリズム
### Basic Information
|  |  |
----|---- 
| Module Name | ProcessingExample |
| Description | ModuleDescription |
| Version | 1.0.0 |
| Vendor | VenderName |
| Category | Category |
| Comp. Type | STATIC |
| Act. Type | PERIODIC |
| Kind | DataFlowComponent |
| MAX Inst. | 1 |
### Activity definition
<table>
  <tr>
    <td rowspan="4">on_initialize</td>
    <td colspan="2">implemented</td>
    <tr>
      <td>Description</td>
      <td></td>
    </tr>
    <tr>
      <td>PreCondition</td>
      <td></td>
    </tr>
    <tr>
      <td>PostCondition</td>
      <td></td>
    </tr>
  </tr>
  <tr>
    <td>on_finalize</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>on_startup</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>on_shutdown</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td rowspan="4">on_activated</td>
    <td colspan="2">implemented</td>
    <tr>
      <td>Description</td>
      <td>onActivatedの動作概要</td>
    </tr>
    <tr>
      <td>PreCondition</td>
      <td></td>
    </tr>
    <tr>
      <td>PostCondition</td>
      <td></td>
    </tr>
  </tr>
  <tr>
    <td rowspan="4">on_deactivated</td>
    <td colspan="2">implemented</td>
    <tr>
      <td>Description</td>
      <td>onDeactivatedの動作概要</td>
    </tr>
    <tr>
      <td>PreCondition</td>
      <td></td>
    </tr>
    <tr>
      <td>PostCondition</td>
      <td></td>
    </tr>
  </tr>
  <tr>
    <td>on_execute</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>on_aborting</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>on_error</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>on_reset</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>on_state_update</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>on_rate_changed</td>
    <td colspan="2"></td>
  </tr>
</table>
### InPorts definition
#### in1
概要説明
<table>
  <tr>
    <td>DataType</td>
    <td>RTC::TimedDoubleSeq</td>
    <td>データ型</td>
  </tr>
  <tr>
    <td>IDL file</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Number of Data</td>
    <td colspan="2">データ数</td>
  </tr>
  <tr>
    <td>Semantics</td>
    <td colspan="2">詳細説明</td>
  </tr>
  <tr>
    <td>Unit</td>
    <td colspan="2">単位</td>
  </tr>
  <tr>
    <td>Occirrence frecency Period</td>
    <td colspan="2">発生頻度、周期</td>
  </tr>
  <tr>
    <td>Operational frecency Period</td>
    <td colspan="2">処理頻度、周期</td>
  </tr>
</table>
#### in2
<table>
  <tr>
    <td>DataType</td>
    <td>RTC::TimedPose2D</td>
    <td></td>
  </tr>
  <tr>
    <td>IDL file</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Number of Data</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Semantics</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Unit</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Occirrence frecency Period</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Operational frecency Period</td>
    <td colspan="2"></td>
  </tr>
</table>
### OutPorts definition
#### out1
概要説明
<table>
  <tr>
    <td>DataType</td>
    <td>RTC::TimedPose3D</td>
    <td>データ型</td>
  </tr>
  <tr>
    <td>IDL file</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Number of Data</td>
    <td colspan="2">データ数</td>
  </tr>
  <tr>
    <td>Semantics</td>
    <td colspan="2">詳細説明</td>
  </tr>
  <tr>
    <td>Unit</td>
    <td colspan="2">単位</td>
  </tr>
  <tr>
    <td>Occirrence frecency Period</td>
    <td colspan="2">発生頻度、周期</td>
  </tr>
  <tr>
    <td>Operational frecency Period</td>
    <td colspan="2">処理頻度、周期</td>
  </tr>
</table>
#### out2
<table>
  <tr>
    <td>DataType</td>
    <td>RTC::TimedPoint3D</td>
    <td></td>
  </tr>
  <tr>
    <td>IDL file</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Number of Data</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Semantics</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Unit</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Occirrence frecency Period</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Operational frecency Period</td>
    <td colspan="2"></td>
  </tr>
</table>
### Service Port definition
#### MyService
概要説明
<table>
  <tr>
    <td>I/F Description</td>
    <td colspan="2">I/F概要説明</td>
  </tr>
  <tr>
    <td colspan="3">Interface</td>
  </tr>
  <tr>
    <td rowspan="9">myservice</td>
    <td>Type</td>
    <td>SimpleService::MyService</td>
    <tr>
      <td>Direction</td>
      <td>Provider</td>
    </tr>
    <tr>
      <td>Description</td>
      <td>概要説明</td>
    </tr>
    <tr>
      <td>IDL file</td>
      <td>MyService.idl</td>
    </tr>
    <tr>
      <td>Argument</td>
      <td>引数</td>
    </tr>
    <tr>
      <td>Return Value</td>
      <td>戻り値</td>
    </tr>
    <tr>
      <td>Exception</td>
      <td>例外</td>
    </tr>
    <tr>
      <td>PreCondition</td>
      <td></td>
    </tr>
    <tr>
      <td>PostCondition</td>
      <td></td>
    </tr>
  </tr>
</table>
### Configuration definition
#### conf1
概要説明
<table>
  <tr>
    <td>DataType</td>
    <td colspan="2">double</td>
  </tr>
  <tr>
    <td>DefaultValue</td>
    <td>0.0</td>
    <td>デフォルト値</td>
  </tr>
  <tr>
    <td>Unit</td>
    <td>m</td>
    <td>単位</td>
  </tr>
  <tr>
    <td>Widget</td>
    <td colspan="2">slider</td>
  </tr>
  <tr>
    <td>Step</td>
    <td colspan="2">0.1</td>
  </tr>
  <tr>
    <td>Constraint</td>
    <td>-1.0<x<1.0</td>
    <td>制約条件</td>
  </tr>
  <tr>
    <td>Range</td>
    <td colspan="2">データ範囲</td>
  </tr>
</table>
#### conf2
<table>
  <tr>
    <td>DataType</td>
    <td colspan="2">int</td>
  </tr>
  <tr>
    <td>DefaultValue</td>
    <td>5</td>
    <td></td>
  </tr>
  <tr>
    <td>Unit</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>Widget</td>
    <td colspan="2">text</td>
  </tr>
  <tr>
    <td>Step</td>
    <td colspan="2"></td>
  </tr>
  <tr>
    <td>Constraint</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>Range</td>
    <td colspan="2"></td>
  </tr>
</table>
## Demo
## Requirement
## Setup
### Windows
### Ubuntu
## Usage
## Running the tests
## LICENCE
ライセンス、使用条件
## References
参考文献
## Author
作成者・連絡先
