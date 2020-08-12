
import $ from 'jquery';

const formatId = (id) => id === 'ALL' ? -1 : (id === 'NONE' ? 0 : parseInt(id));

export default {
  list: (type) => {
    return $.ajax({
      url: `/folder/list?type=${type}`,
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json;charset=UTF-8"
      }
    });
  },

  create: (name, parent, type) => {
    parent = formatId(parent);
    return $.ajax({
      url: `/folder/create`,
      type: 'POST',
      data: JSON.stringify({
        name: name,
        type: type,
        parent: parent == -1 ? 0 : parent
      }),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json;charset=UTF-8"
      }
    });
  },

  update: (data) => {
    const { id, name, type, parent } = data;
    return $.ajax({
      url: `/folder/update`,
      type: 'POST',
      data: JSON.stringify({
        name: name,
        type: type,
        parent: formatId(parent),
        id: formatId(id)
      }),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json;charset=UTF-8"
      }
    });
  },

  moveTo: (folderId, contentIds, type) => {
    return $.ajax({
      url: `/folder/addToFolder`,
      type: 'POST',
      data: JSON.stringify({
        folderId: formatId(folderId),
        contents: contentIds,
        type: type,
      }),
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json;charset=UTF-8"
      }
    });
  },

  remove: (id) => {
    return $.ajax({
      url: `/folder/remove?id=${formatId(id)}`,
      type: 'DELETE',
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json;charset=UTF-8"
      }
    });
  },

  getFolderId: (type, contentId) => {
    return $.ajax({
      url: `/folder/queryFolderIdByTypeAndContent?type=${type}&contentId=${contentId}`,
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json;charset=UTF-8"
      }
    });
  },
};
