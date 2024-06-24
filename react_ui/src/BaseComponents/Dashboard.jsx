//TODO: Add view program here.

import { useNavigate } from "react-router-dom";
import LogoutComponent from "../Login/LogoutComponent";

const HomePage = ({ title, dashStructure, sub }) => {

  const navigate = useNavigate();

  return (
    <div className="component-container">
      <h1>{title}</h1>
      <div className="dash-container">
        {dashStructure.map((menu) => {
          return <div key={menu.link}>
            <button
              type="button"
              onClick={() => navigate((sub ? "" : "../") + menu.link, { relative: "path" })}>
              {menu.name}
            </button>
          </div>
        })}
        {!sub ? (<><div>
          <button className='change-user-details' type="button"
            onClick={() => navigate("../" + "change-user-details", { relative: "path" })}>Change my user details.</button>
        </div>
          <LogoutComponent />
        </>) : <div><button type="button"
          onClick={() => navigate("../", { relative: "path" })}>Go back.</button>
        </div>}
      </div>
    </div>
  )
}

export default HomePage