import { useContext, useEffect, useState } from 'react'
import useAxiosPrivate from '../Login/useAxiosPrivate';
import { useNavigate } from 'react-router-dom';
import SearchBar from './SearchBar';
import Form from './Form';
import UserContext from '../Context/UserProvider';
import GradePage from '../SpecialComponents/GradePage';
import StatsTable from '../SpecialComponents/StatsTable';
import Program from '../SpecialComponents/Program';

const SelectList = ({ title,
    requestURL,
    requestIds,
    successMsg,
    buttonMsg,
    formStructure,
    fetchUrl,
    deleteWarningMsg,
    deleteUrl,
    isPut,
    isDelete
}) => {

    const [formData, setFormData] = useState({});
    const [fetchedSelections, setFetchedSelections] = useState([]);
    const [errMsg, setErrMsg] = useState("");
    const [success, setSuccess] = useState(true);
    const [level, setLevel] = useState(0)
    const [search, setSearch] = useState('');
    const [loading, setLoading] = useState(true);

    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();
    const { id } = useContext(UserContext);

    const formatString = (string, params) => {
        const idReplacedString = string.replace("{id}", id)
        return idReplacedString.replace(/{(\d+)}/g, (match, index) => {
            return typeof params[index] !== 'undefined' ? params[index] : match;
        });
    }

    const handleBack = () => {
        if (level <= 0)
            navigate("../", { relative: "path" });
        else {
            setLoading(true)
            const fixedFormData = Object.keys(formData).reduce((acc, key) => {
                if (key != formStructure[level - 1].id) acc[key] = formData[key];
                return acc
            }, {})
            setFormData(fixedFormData)
            setLevel(level - 1)
        }
    }

    const generateYearsArray = (startYear) => {
        const years = [];
        for (let year = startYear; year >= 2023; year--) {
            if (formStructure[level].isNumber) {
                years.push({
                    id: year,
                    year: `${year}/${year + 1}`
                });
            }
            else {
                years.push({
                    id: year + 'S',
                    year: `${year}/${year + 1} Spring`
                });
                years.push({
                    id: year + 'A',
                    year: `${year}/${year + 1} Autumn`
                });
            }
        }
        return years;
    }

    const handleForward = (id) => {
        setFormData({ ...formData, [formStructure[level].id]: id });
        setLoading(true)
        setLevel(level + 1)
    }

    const handleApplySelection = () => {
        if (formData[formStructure[level].id]?.length > 0) {
            setLoading(true)
            setLevel(level + 1)
        }
        else {
            setSuccess(false)
            setErrMsg("Please select at least one before applying.")
        }
    }

    const handleSelection = (target, id) => {
        if (formData[formStructure[level].id]?.length > 0
            && formData[formStructure[level].id].includes(id)) {
            const newArr = formData[formStructure[level].id].filter(item => item !== id)
            setFormData({ ...formData, [formStructure[level].id]: newArr });
        }
        else {
            const newArr = formData[formStructure[level].id] ? [...formData[formStructure[level].id], id] : [id]
            setFormData({ ...formData, [formStructure[level].id]: newArr });
        }
    }

    const handleSkip = (skiplvl = 1) => {
        setLoading(true)
        setLevel(level + skiplvl)
    }

    useEffect(() => {
        if (!formStructure[level]?.type)
            fetchOptions();
    }, [level])

    const fetchOptions = async () => {
        setSuccess(true)
        if (formStructure[level].fetchUrl == "year") {
            const yeararr = generateYearsArray(new Date().getFullYear())
            setFetchedSelections(yeararr);
            setLoading(false)
        }
        else {
            let semester = "";
            const replace = Object.entries(formData).filter(([key, value]) => {
                return formStructure[level]?.require ? formStructure[level].require.includes(key) : false
            }).map(([key, value]) => {
                if (key == "year") {
                    semester = value.slice(4);
                    return value.slice(0, 4);
                }
                return value
            })
            const url = formStructure[level].require ?
                formatString(formStructure[level].fetchUrl, replace)
                : formStructure[level].fetchUrl;

            try {
                const res = await axiosPrivate.get(url);
                let dataArray;
                if (semester) {
                    const filteredRes = res.data.filter((data) => {
                        return semester == 'A' ? data.autumn : !data.autumn
                    })
                    dataArray = filteredRes;
                }
                else dataArray = res.data;

                if (formStructure[level]?.followUpUrl) {
                    const urlTwo = formStructure[level].require ?
                        formatString(formStructure[level].followUpUrl, replace)
                        : formStructure[level].followUpUrl;

                    const resTwo = await axiosPrivate.get(urlTwo);

                    let placeholderData = [];
                    let placeholderDataEmail = [];
                    if (formStructure[level].replacement == "FLNAME") {
                        resTwo.data.forEach((data) => {
                            placeholderData[data.id] = data.firstname + " " + data.lastname;
                            placeholderDataEmail[data.id] = data.email;
                        })
                    }
                    else if (formStructure[level].replacement == "COURSE") {
                        resTwo.data.forEach((data) => {
                            placeholderData[data.id] = data.signature + " " + data.name;
                        })
                    }

                    dataArray.forEach((data) => {
                        const id = data[formStructure[level].fetchLabel];
                        data[formStructure[level].fetchLabel] = placeholderData[id]
                        if (placeholderDataEmail.length) {
                            data.email = placeholderDataEmail[id]
                        }
                    })
                }

                setFetchedSelections(dataArray)

                setLoading(false)
            } catch (err) {
                setLoading(false)
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
    if (!formStructure[level]?.type)
        return (
            <div className='component-container' style={{ paddingBottom: "1.5rem" }}>
                {title ? <h1>{title}</h1> : <></>}
                {formStructure[level]?.selectMsg ? <h3>{formStructure[level].selectMsg}</h3> : <></>}
                {!errMsg ? <></> : <p className="err-msg" style={success ? {} : { color: "red" }}>{errMsg}</p>}
                <SearchBar
                    search={search}
                    setSearch={setSearch}
                />
                <div className='select-list'>
                    {formStructure[level]?.skip && <div className="btn skip-btn">
                        <button type='button' onClick={() => handleSkip(formStructure[level]?.skiplvl)}>
                            {formStructure[level]?.skip}
                        </button>
                    </div>}
                    {formStructure[level]?.multi && <div className="btn skip-btn">
                        <button type='button' onClick={() => handleApplySelection()}>
                            Apply Selection
                        </button>
                    </div>}
                    {loading && <p>Loading...</p>}

                    {!loading && fetchedSelections.length ? (
                        fetchedSelections.filter((name) => (
                            (name[formStructure[level].fetchLabel] ? name[formStructure[level].fetchLabel] : "").toLowerCase()).includes(search.toLowerCase()
                            )).sort().map((item) => {
                                return <section key={item.id}
                                    className={"select-list-selection" + ((formData[formStructure[level].id] ?? []).includes(item.id) ? " selected-from-list" : "")}
                                    onClick={formStructure[level].multi ? (e) => handleSelection(e.target, item.id) : () => handleForward(formStructure[level]?.altId ? item[formStructure[level].altId] : item.id)}>
                                    <p style={{ fontWeight: "600" }}>{item[formStructure[level].fetchLabel] + (formStructure[level].fetchLabelAdd ? " " + item[formStructure[level].fetchLabelAdd] : "")}</p>
                                    <p>{item[formStructure[level].fetchLabelSecond]}</p>
                                </section>
                            })) : <p>There are no options available.</p>}

                    {!loading && <div className="btn">
                        <button type='button' onClick={() => handleBack()}>
                            Go Back
                        </button>
                    </div>}
                </div>
            </div>
        )
    else if (formStructure[level].type == "FORM") {
        const reqIds = Object.entries(formData).filter(([key, value]) => {
            return requestIds ? requestIds.includes(key) : false
        }).reduce((acc, [key, value]) => {
            acc.push(value)
            return acc
        }, [])
        return (
            <Form
                title={title}
                requestURL={requestURL}
                requestIds={reqIds}
                successMsg={successMsg}
                buttonMsg={buttonMsg}
                formStructure={formStructure[level].data}
                selectListValues={{ ...formData }}
                fetchUrl={fetchUrl}
                deleteWarningMsg={deleteWarningMsg}
                deleteUrl={deleteUrl}
                isPut={isPut}
                isDelete={isDelete}
                backFunc={handleBack} />
        )
    }
    else if (formStructure[level].type == "GRADE") {
        return (
            <GradePage
                title={title}
                selectListValues={{ ...formData }}
                backFunc={handleBack} />
        )
    }
    else if (formStructure[level].type == "PROGRAM") {
        return (
            <Program
                title={title}
                backFunc={handleBack}
                selectListValues={{ ...formData }} />
        )
    }
    else if (formStructure[level].type == "STATS") {
        const match = fetchedSelections.find((item) => item.id == formData[formStructure[level - 1].id])
        return (
            <StatsTable
                title={title}
                name={match[formStructure[level - 1].fetchLabel] + (formStructure[level - 1].fetchLabelAdd ? " " + match[formStructure[level - 1].fetchLabelAdd] : "")}
                require={[formStructure[level].statistic, formData[formStructure[level - 1].id]]}
                backFunc={handleBack} />
        )
    }
    else
        return (
            <>{!loading && <div className="btn">
                <button type='button' onClick={() => handleBack()}>
                    Go Back
                </button>
            </div>}</>
        )
}

export default SelectList