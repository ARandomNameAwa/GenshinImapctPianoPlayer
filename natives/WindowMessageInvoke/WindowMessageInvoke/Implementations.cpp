#include "pch.h"
#include "Implementations.h"

///< 枚举窗口回调函数
BOOL CALLBACK EnumWindowsProc(HWND hwnd, LPARAM lParam)
{
	EnumWindowsArg* pArg = (EnumWindowsArg*)lParam;
	DWORD dwProcessID = 0;
	// 通过窗口句柄取得进程ID
	::GetWindowThreadProcessId(hwnd, &dwProcessID);
	if (dwProcessID == pArg->dwProcessID)
	{
		pArg->hwndWindow = hwnd;
		// 找到了返回FALSE
		return FALSE;
	}
	// 没找到，继续找，返回TRUE
	return TRUE;
}
///< 通过进程ID获取窗口句柄
HWND GetWindowHwndByPID(DWORD dwProcessID)
{
	HWND hwndRet = NULL;
	EnumWindowsArg ewa;
	ewa.dwProcessID = dwProcessID;
	ewa.hwndWindow = NULL;
	EnumWindows(EnumWindowsProc, (LPARAM)&ewa);
	if (ewa.hwndWindow)
	{
		hwndRet = ewa.hwndWindow;
	}
	return hwndRet;
}

