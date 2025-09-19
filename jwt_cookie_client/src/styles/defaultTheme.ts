const defaultTheme = {
  palette: {
    mode: "light", // 'light' 또는 'dark' 모드 설정
    background: {
      default: "#FFF", // 배경 메인 (backgroundMain)
      paper: "#E2EFF2", // 배경 서브 (backgroundThird)
    },
    text: {
      primary: "#222", // 기본 텍스트 색상 (textMain)
      secondary: "#7A7676", // 서브 텍스트 색상 (textSecond)
      disabled: "#BDBDBD", // 비활성화된 텍스트 색상
    },
    primary: {
      main: "#00A9E6", // 주요 색상 (primary)
      dark: "#008EC1", // 어두운 색상 (primaryDeep)
      light: "#33C3F2", // 밝은 색상
      contrastText: "#FFF", // 대비 텍스트 색상
    },
    secondary: {
      main: "#0000FF", // 보조 색상 (secondary)
      contrastText: "#FFF", // 대비 텍스트 색상
    },
    third: {
      Light: "#ede7f6",
      Main: "#673ab7",
      Dark: "#5e35b1",
      50: "#f3eafc",
      100: "#e0d4f5",
      200: "#c7b3ec",
      300: "#ab8fe3",
      400: "#8f6ad9",
      600: "#5e35b1",
      700: "#512da8",
      800: "#4527a0",
      900: "#311b92",
    },
    fourth: {
      Light: "#e3f2fd",
      Main: "#2196f3",
      Dark: "rgba(0, 107, 201, 1)",
      200: "#90caf9",
      800: "#074fa1ff",
      1000: "#0a468aff",
    },
    error: {
      main: "#FF4D4F", // 에러 색상
    },
    warning: {
      main: "#FFC107", // 경고 색상
    },
    info: {
      main: "#2196F3", // 정보 색상
    },
    success: {
      main: "#4CAF50", // 성공 색상
    },
    divider: "#555", // 구분선 및 그림자 색상 (shadowMain)
    action: {
      active: "#222", // 활성화된 요소의 색상
      hover: "#ECECEC", // hover 시 색상 (buttonMain)
      hoverOpacity: 0.1, // hover 시 불투명도
      selected: "#D3E4F5", // 선택된 요소의 배경 색상
      selectedOpacity: 0.2, // 선택된 요소의 불투명도
      disabled: "#BDBDBD", // 비활성화된 요소의 색상
      disabledBackground: "#F5F5F5", // 비활성화된 요소의 배경 색상
      focus: "#00A9E6", // 포커스된 요소의 색상
      focusOpacity: 0.3, // 포커스된 요소의 불투명도
      activatedOpacity: 0.2, // 활성화된 요소의 불투명도
    },
    grey: {
      100: "#F5F5F5", // buttonMain과 비슷한 중립 색상
      200: "#ECECEC",
      300: "#E0E0E0",
      400: "#BDBDBD",
      500: "#9E9E9E",
      600: "#757575",
      700: "#616161",
      800: "#424242",
      900: "#212121",
    },
  },
  shape: {
    borderRadius: 10, // 모서리 둥글기 설정 (px.radius)
  },
  shadows: [
    "none",
    "0px 1px 3px rgba(0,0,0,0.1)", // 그림자 설정 (shadowMain)
    "0px 1px 3px rgba(0,0,0,0.2)", // 그림자 설정 (shadowMain)
    "0px 1px 5px rgba(0,0,0,0.3)",
    "0px 1px 8px rgba(0,0,0,0.4)",
    "0px 1px 10px rgba(0,0,0,0.5)",
    "0px 1px 10px rgba(255, 255, 255, 0.61)",
  ],
};

export default defaultTheme;
