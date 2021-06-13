# GenshinImapctPianoPlayer #
#### <s>一个原神的自动弹琴脚本</s>    基本能用在所有能用键盘弹琴的游戏的自动弹琴脚本 ####     
新人第一次在github上上传项目 请大佬们多多指教
### 用法:
  File Path: 要加载的mid或txt文件的路径   
  Speed: 演奏音乐的速度 单位是刻每秒(tps)     
  Parser: 解析文件的解析器,一般会根据所输入的文件后缀自动匹配     
  Player: 演奏音乐的演奏器,会根据解析器解析出的音乐类型自动匹配     
  Key Map: 使用的音符-键盘映射    
  Start:开始演奏  
  Pause:暂停或继续演奏  
  Stop:停止演奏  
### 右侧信息栏:
  Actual Speed: 演奏的真实速度,卡顿时这个数值会下降  
  Current Tick: 进度条,横杠左面的是目前的tick,右面的是总tick  
### 自定义音符-键盘映射
  在jar包的目录下创建一个叫做keyMaps的文件夹,在里面创建一个txt文件,   
  里面按照以下形式填写音符和它对应的按键:    
  ``` 音名+音高:按键 ```
  例如:   
  ``` 
  C4:a
  D4:s
  ```
  然后保存并重启程序,你就能在keyMap选项栏里看到你的音符-按键映射文件了
  
  
  
### 注意事项:
  1.在演奏时改变演奏速度只有暂停然后继续或者重新开始演奏才会生效  
  2.如果在输入完文件路径后如果没有反应,按一下回车就好了    
  3.如果在创建完音符-按键映射文件后无法在程序的keyMap选项栏里找到,有可能是你的文件写的有错误      
   
      可能的错误:冒号要用英文冒号
  
