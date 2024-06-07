//placeholder homepage

import { useNavigate } from "react-router-dom";
import LogoutComponent from "../Login/LogoutComponent";

const HomePage = ({ title, dashStructure }) => {

  const navigate = useNavigate();

  return (
    <div className="component-container">
      <h1>{title} Dashboard</h1>
      <div className="dash-container">
        {dashStructure.map((menu) => {
          return <div key={menu.link}>
            <button
              type="button"
              onClick={() => navigate("../" + menu.link, { relative: "path" })}>
              {menu.name}
            </button>
          </div>
        })}
        <LogoutComponent />
      </div>
    </div>
  )
}

export default HomePage