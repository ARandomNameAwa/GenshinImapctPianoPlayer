// dllmain.cpp : 定义 DLL 应用程序的入口点。

#include "pch.h"
#include <vector>
#include <set>


BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
                     )
{
    switch (ul_reason_for_call)
    {
    case DLL_PROCESS_ATTACH:
    case DLL_THREAD_ATTACH:
    case DLL_THREAD_DETACH:
    case DLL_PROCESS_DETACH:
        break;
    }
    return TRUE;
}
MYLIBAPI BOOL sendKeyBoardMessageToWindow(wchar_t* windowTitle, int key, int state){
    return true;
}
std::set<std::string>* titles;

MYLIBAPI  wchar_t* listWindows(){
    titles = new std::set<std::string>();
    EnumWindows(EnumWindowsProc, NULL);
    std::string result = "";
    std::set<std::string>::iterator itr = titles->end();
    for (std::set<std::string>::iterator itrBegin = titles->begin(); itrBegin != itr; ++itrBegin) {
        std::string ele = *itrBegin;
        if (ele.empty())
        {
            continue;
        }
        result += ele;
        result += ";";
    }
    delete titles;
    return   multiByteToWideChar(result);
}

std::string TCHAR2STRING(TCHAR* STR)
{
 int iLen = WideCharToMultiByte(CP_ACP, 0,STR, -1, NULL, 0, NULL, NULL);   //首先计算TCHAR 长度。

 char* chRtn = new char[iLen * sizeof(char)];  //定义一个 TCHAR 长度大小的 CHAR 类型。

 WideCharToMultiByte(CP_ACP, 0, STR, -1, chRtn, iLen, NULL, NULL);  //将TCHAR 类型的数据转换为 CHAR 类型。

std::string str(chRtn); //最后将CHAR 类型数据 转换为 STRING 类型数据。

return str;

}

BOOL CALLBACK EnumWindowsProc(
	HWND hwnd,
	LPARAM lParam)
{
    if (IsWindowVisible(hwnd)) {
        WINDOWPLACEMENT ws;
        ws.length = sizeof(WINDOWPLACEMENT);
        GetWindowPlacement(hwnd, &ws);
        if (ws.showCmd !=2)
        {
            TCHAR caption[200];
            memset(caption, 0, sizeof(caption));
            ::GetWindowText(hwnd, caption, 200);
            std::string name = TCHAR2STRING(caption);
            if (strcmp(name.c_str(), ""))
                std::cout << name << std::endl;
                titles->insert(name);
            }
        }
	return TRUE;

}

wchar_t* multiByteToWideChar(const std::string& pKey)
{
    const char* pCStrKey = pKey.c_str();
    //第一次调用返回转换后的字符串长度，用于确认为wchar_t*开辟多大的内存空间
    int pSize = MultiByteToWideChar(CP_OEMCP, 0, pCStrKey, strlen(pCStrKey) + 1, NULL, 0);
    wchar_t* pWCStrKey = new wchar_t[pSize];
    //第二次调用将单字节字符串转换成双字节字符串
    MultiByteToWideChar(CP_OEMCP, 0, pCStrKey, strlen(pCStrKey) + 1, pWCStrKey, pSize);
    return pWCStrKey;
}
