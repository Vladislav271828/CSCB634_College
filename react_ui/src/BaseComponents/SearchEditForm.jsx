import { useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';
import Form from './Form';

const SearchEditForm = ({ title, requestURL, successMsg, buttonMsg, formStructure, fetchUrl, searchLabel }) => {

    const [formData, setFormData] = useState("");
    const [fetchedEditValues, setFetchedEditValues] = useState(null);
    const [newFetchUrl, setNewFetchUrl] = useState(fetchUrl);
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();

    const handleChange = (e) => {
        setErrMsg("");
        setFormData(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSuccess(true)
        try {
            // TODO: remove debug and make functional
            // const res = await axiosPrivate.get(fetchUrl);
            // console.log(res.data);
            // setFetchedEditValues(res);


            setFetchedEditValues({
                id: 0,
                firstname: "suck",
                lastname: "much",
                email: "ida@abv.bg",
                role: "STUDENT"
            });
        } catch (err) {
            setSuccess(false)
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
                console.log(err)
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    }

    return (
        <div className='component-container'>
            {title ? <h1>{title}</h1> : <></>}
            {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}

            <form className='form-structure-container' onSubmit={handleSubmit}>
                <div className='input-with-button'>
                    <label>{searchLabel}</label>
                    <div>
                        <input type="text"
                            name="id"
                            onChange={handleChange}
                        />
                        <button type='submit'>
                            Search
                        </button>
                    </div>
                </div>
                {fetchedEditValues ? <></> : <div className="btn">
                    <button type='button' onClick={() => navigate("../dashboard", { relative: "path" })}>
                        Go Back
                    </button>
                </div>}
            </form>
            {fetchedEditValues ? <Form
                requestURL={requestURL}
                requestEditId={formData}
                successMsg={successMsg}
                buttonMsg={buttonMsg}
                formStructure={formStructure}
                editValues={fetchedEditValues} /> : <></>}
        </div>

    )
}

export default SearchEditForm