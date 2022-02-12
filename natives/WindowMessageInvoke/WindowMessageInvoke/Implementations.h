#pragma once
#include <Windows.h>
typedef struct
{
	HWND hwndWindow; // 窗口句柄
	DWORD dwProcessID; // 进程ID
}EnumWindowsArg;
HWND GetWindowHwndByPID(DWORD dwProcessID);


BOOL CALLBACK EnumWindowsProc(HWND hwnd, LPARAM lParam);