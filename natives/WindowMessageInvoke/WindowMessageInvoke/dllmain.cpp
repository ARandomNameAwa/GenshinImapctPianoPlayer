// dllmain.cpp : 定义 DLL 应用程序的入口点。
#include "pch.h"

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

extern "C" __declspec(dllexport) bool sendKeyBoardMessageToWindow(wchar_t* windowName,int key,int state) {
    HWND windowHandler = ::FindWindow(NULL,windowName);
    return true;
}
extern "C" __declspec(dllexport) wchar_t** getWindowNamesOfTheProcess() {
    return NULL;
}
