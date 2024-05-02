//placeholder homepage

import { Link } from "react-router-dom";

function HomePage() {
  return (
    <div>
      <p>You have logged in successfully</p>
      <Link
        to="/login"
        tabIndex={0}><button
          type="button"
        > Go back
        </button></Link>

    </div>
  )
}

export default HomePage