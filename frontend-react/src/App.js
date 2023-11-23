import logo from "./logo.svg";
import "./App.css";
import "./index.css";

function App() {
  return (
    <div className="App bg-white flex flex-col items-center h-screen">
      <img src="logo.png" alt="Main" className="h-36" />
      <img
        src="qrcode_play.google.com.png"
        alt="QR Code"
        className="h-52 w-52"
      />

      <a
        href="https://zep.us/play/yOZ9WR"
        target="_blank"
        className="absolute top-4 right-4"
      >
        <img src="symbol_wordmark_light.png" alt="Link" className="h-12" />
      </a>
    </div>
  );
}

export default App;
