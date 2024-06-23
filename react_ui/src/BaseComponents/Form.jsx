//sisyphus must be imagined happy the same way i must be imagined happy dealing with all this mess
import React, { useContext, useEffect, useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';
import UserContext from '../Context/UserProvider';

const Form = ({ title,
    requestURL,
    requestIds,
    successMsg,
    buttonMsg,
    formStructure,
    fetchUrl,
    editValues,
    deleteWarningMsg,
    deleteUrl,
    //it would be better for it to be a request type and use a switch case
    isPut,
    isDelete,
    backFunc,
    selectListValues }) => {

    const [formData, setFormData] = useState(selectListValues ? selectListValues : {});
    const [formDataNoSend, setFormDataNoSend] = useState(selectListValues ? { ...selectListValues } : {});
    const [fetchedEditValues, setFetchedEditValues] = useState(editValues ? editValues : (selectListValues ? selectListValues : {}));
    const [fetchedSelections, setFetchedSelections] = useState({});
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();
    const { id } = useContext(UserContext);

    function formatString(string, params) {
        const yearReplacedString = formDataNoSend?.year ? string.replace("{year}", formDataNoSend.year.slice(0, 4)) : string
        const idReplacedString = yearReplacedString.replace("{id}", id)
        console.log(idReplacedString)
        return idReplacedString.replace(/{(\d+)}/g, (match, index) => {
            return typeof params[index] !== 'undefined' ? params[index] : match;
        });
    }

    function containsAllElements(arr1, arr2) {
        return arr2.every(element => arr1.includes(element));
    }

    const handleChange = (e) => {
        setErrMsg("");
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleYearChange = (e) => {
        setErrMsg("");
        if (e.target.value.includes('A'))
            setFormData({ ...formData, date: e.target.value.match(/\d+/g)[0] + "-10-01", autumn: true });
        else
            setFormData({ ...formData, date: e.target.value.match(/\d+/g)[0] + "-10-01", autumn: false });
        console.log(formData)
    };

    function generateYearsArray(startYear) {
        const years = [];
        for (let year = startYear; year >= 2023; year--) {
            years.push(year);
        }
        return years;
    }

    const handleBack = () => {
        if (backFunc) backFunc();
        else
            navigate("../dashboard", { relative: "path" });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        setSuccess(true)
        let yearFixFormData = formData;
        if (formData?.year) yearFixFormData = { ...formData, year: new Number(formData.year.slice(0, 4)) }
        const url = formatString(requestURL, requestIds);
        try {
            var res
            if (isDelete) {
                res = await axiosPrivate.delete(url, { data: yearFixFormData });
                alert("Operation successful.");
                navigate("../dashboard", { relative: "path" })
            }
            else
                isPut ?
                    res = await axiosPrivate.put(url, yearFixFormData)
                    : res = await axiosPrivate.post(url, yearFixFormData)
            // console.log(res.data);
            setErrMsg(successMsg);
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

    const fetchEditValues = async () => {
        const url = formatString(fetchUrl, requestIds);
        try {
            const res = await axiosPrivate.get(url);
            // console.log(res.data);
            setFetchedEditValues(res.data);
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

    const handleDelete = async () => {
        if (confirm("Are you sure you want to " + deleteWarningMsg + "?\nThis action is irreversible.") == true) {
            try {
                const url = formatString(deleteUrl, requestIds);
                await axiosPrivate.delete(url);
                alert("Operation successful.");
                navigate("../dashboard", { relative: "path" })
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
    }

    useEffect(() => {
        formStructure.map((input) => {
            if (input.type == "select" && input.fetchUrl && (!input.require || containsAllElements(Object.keys(formData), input.require))) {
                fetchOptions(input)
            }
        });
        const ids = formStructure.map((item) => item.id);
        const selectedValues = Object.keys(formData).reduce((acc, key) => {
            if (ids.includes(key)) acc[key] = formData[key];
            return acc
        }, {})
        setFormData(selectedValues)
        if (fetchUrl) fetchEditValues();
    }, [])

    const fetchOptions = async (input) => {
        setSuccess(true)
        console.log(requestIds)
        const yearReplacedString = formDataNoSend?.year ? input.fetchUrl.replace("{year}", formDataNoSend.year.slice(0, 4)) : input.fetchUrl
        const idReplacedString = yearReplacedString.replace("{id}", id)
        const url = idReplacedString.replace("{0}", formData[input.require]);
        try {
            const res = await axiosPrivate.get(url);
            //state updater saving lives
            setFetchedSelections(fs => ({ ...fs, [input.id]: res.data }))
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
                {formStructure.map((input) => {
                    if ((formDataNoSend[input.require] || !input.require) && !input?.hide) {
                        if (input.type == "select") {
                            return <div key={input.id} style={input.disabled && { opacity: "0.5" }}>
                                <label>{input.label}</label>
                                <select
                                    name={input.id}
                                    onChange={handleChange}
                                    disabled={input.disabled}
                                    multiple={input.multi}
                                >
                                    <option value={""} selected disabled hidden></option>
                                    {input.fetchUrl ?
                                        fetchedSelections[input.id]?.map((selection) => {
                                            if (!input.multi || (input.multi && formDataNoSend[input.id].includes(selection.id))) return <option key={selection.id}
                                                value={selection.id}
                                                selected={!input.multi && ((selection.id == fetchedEditValues[input.id] && !formDataNoSend[input.id])
                                                    || (selection.id == formDataNoSend[input.id]))
                                                }>{selection[input.fetchLabel] + (selection[input.fetchLabelAdd] ? " " + selection[input.fetchLabelAdd] : "")}</option>
                                        })
                                        : Object.entries(input.options).map(([value, text]) => {
                                            return <option key={value}
                                                value={value}
                                                selected={(value == fetchedEditValues[input.id] && !formDataNoSend[input.id])
                                                    || (!fetchedEditValues[input.id] && value == formDataNoSend[input.id])
                                                }>{text}</option>
                                        })}
                                </select>
                            </div>
                        }
                        else if (input.type == "year") {
                            return <div key={input.id} style={input.disabled && { opacity: "0.5" }}>
                                <label>{input.label}</label>
                                <select
                                    name={input.id}
                                    onChange={handleYearChange}
                                    disabled={input.disabled}

                                >
                                    <option value={""} selected disabled hidden></option>
                                    {generateYearsArray(new Date().getFullYear()).map((year) => {
                                        return <React.Fragment key={year}>
                                            <option
                                                value={year + 'S'}
                                                selected={(year == new Date(fetchedEditValues.date).getFullYear && fetchedEditValues.autumn == false)
                                                    || formDataNoSend[input.id] == year + 'S'
                                                }>{year + "/" + Number(year + 1) + ' Spring'}</option>
                                            <option
                                                value={year + 'A'}
                                                selected={(year == new Date(fetchedEditValues.date).getFullYear && fetchedEditValues.autumn == true)
                                                    || formDataNoSend[input.id] == year + 'A'
                                                }>{year + "/" + Number(year + 1) + ' Autumn'}</option>
                                        </React.Fragment>
                                    })}
                                </select>
                            </div>
                        }
                        else {
                            return <div key={input.id} style={input.disabled && { opacity: "0.5" }}>
                                <label >{input.label}</label>
                                <input type={input.type}
                                    name={input.id}
                                    defaultValue={fetchedEditValues[input.id]}
                                    onChange={handleChange}
                                    disabled={input.disabled}
                                />
                            </div>
                        }
                    }
                })}
                <div className="btn">
                    <button type='submit' className={isDelete ? "logout-btn" : ""}>
                        {buttonMsg}
                    </button>
                    <button type='button' onClick={() => handleBack()}>
                        Go Back
                    </button>
                </div>
            </form>
            {deleteUrl ? <div className='form-structure-container'>
                <div className="btn big-delete-btn">
                    <button type='button' onClick={() => handleDelete()}>
                        Delete
                    </button>
                </div>
            </div> : <></>}
        </div>

    )
}

export default Form