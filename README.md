# chatwithcmd
用命令行工具聊天
<h3>用cmd聊天
软件的运行流程如下：
1.运行bat文件，打开cmd窗口
2.在局域网中广播
3.无响应则自己成为会话的管理员
4.有响应则向管理员发送加入会话请求
5.管理员允许之后向会员群播新加入的会员地址
6.会员发送消息先送到管理员这里，管理员再群播到各会员
7.管理员退出回话，第一个发现此事的会员向各会员广播这一消息，并将会员地址队列中的下一个会员作为管理员
待解决问题：
1.客户机需要响应每一条的消息吗
2.