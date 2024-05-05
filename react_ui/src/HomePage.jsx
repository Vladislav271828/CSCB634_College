//placeholder homepage

import { Link } from "react-router-dom";
import LogoutComponent from "./Login/LogoutComponent";

function HomePage() {
  return (
    <div style={{ margin: "10vh" }}>
      <p>You have logged in successfully</p>
      <Link
        to="/login"
        tabIndex={0}><button
          type="button"
        > Go back
        </button></Link>
      <LogoutComponent />

    </div>
  )
}

export default HomePage